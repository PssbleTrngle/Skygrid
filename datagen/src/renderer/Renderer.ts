import rawCanvas from 'canvas'
import { existsSync, readdirSync, readFileSync, statSync } from 'fs'
import { ensureDirSync } from 'fs-extra'
import { merge } from 'lodash'
//@ts-ignore
import { createCanvas, loadImage } from 'node-canvas-webgl'
import { join, parse } from 'path'
import {
   BoxGeometry,
   DirectionalLight,
   Material,
   MathUtils,
   Mesh,
   MeshBasicMaterial,
   NearestFilter,
   OrthographicCamera,
   Scene,
   Texture,
   Vector3,
   WebGLRenderer,
} from 'three'
import { Named } from 'types'
import { size } from './math'
import { AnimationMeta, BlockModel, BlockSides, Element, Face } from './types'

const FACES = ['east', 'west', 'up', 'down', 'south', 'north'] as const

const BUILTIN: BlockModel = {
   display: {
      gui: {
         rotation: [-15, -90, 0],
         scale: [0.625, 0.625, 0.625],
         translation: [0, 0, 0],
      },
   },
   elements: [
      {
         from: [0, 0, 0],
         to: [16, 16, 16],
         faces: {
            up: { texture: '#layer0' },
            down: { texture: '#layer0' },
            north: { texture: '#layer0' },
            east: { texture: '#layer0' },
            south: { texture: '#layer0' },
            west: { texture: '#layer0' },
         },
      },
   ],
}

export default class Renderer {
   private size = 512
   private distance = 15

   private scene = new Scene()
   private camera = new OrthographicCamera(
      -this.distance,
      this.distance,
      this.distance,
      -this.distance,
      0.01,
      20000
   )
   private canvas = createCanvas(this.size, this.size)
   private renderer = new WebGLRenderer({
      canvas: this.canvas,
      alpha: true,
      antialias: true,
      logarithmicDepthBuffer: true,
   })

   constructor(private readonly dir: string) {
      ensureDirSync(dir)
      const light = new DirectionalLight(0xffffff, 1.2)
      light.position.set(-15, 30, -25)
      this.scene.add(light)
      this.renderer.sortObjects = false
   }

   async getBlocks(): Promise<Named[]> {
      const namespaces = readdirSync(this.dir).filter(it =>
         statSync(join(this.dir, it)).isDirectory()
      )

      return namespaces.flatMap(mod => {
         const modelsDir = join(this.dir, mod, 'blockstates')
         if (!existsSync(modelsDir)) return []
         const models = readdirSync(modelsDir).filter(it => it.endsWith('.json'))
         return models.map(file => ({ mod, id: parse(file).name }))
      })
   }

   async render(block: Named) {
      const model = this.getModel(block, 'item')
      return this.renderModel(model)
   }

   async renderModel(model: BlockModel) {
      const { elements, display } = model
      const { gui } = display ?? {}
      if (!gui) throw new Error('No gui configuration')
      if (!elements) throw new Error('No elements')

      this.camera.zoom = 1 / Math.sqrt(gui.scale[0] ** 2 + gui.scale[1] ** 2 + gui.scale[2] ** 2)

      this.scene.clear()

      await Promise.all(
         elements.map(async (element, i) => {
            const calculatedSize = size(element.from, element.to)

            const geometry = new BoxGeometry(...calculatedSize, 1, 1, 1)
            const materials = await this.constructBlockMaterial(model, element)
            const cube = new Mesh(geometry, materials)
            cube.position.set(0, 0, 0)
            cube.position.add(new Vector3(...element.from))
            cube.position.add(new Vector3(...element.to))
            cube.position.multiplyScalar(0.5)
            cube.position.add(new Vector3(-8, -8, -8))

            cube.renderOrder = i

            this.scene.add(cube)
         })
      )

      const rotation = new Vector3(...gui.rotation).add(new Vector3(195, -90, -45))
      this.camera.position.set(
         ...(rotation.toArray().map(x => Math.sin(x * MathUtils.DEG2RAD) * 16) as [
            number,
            number,
            number
         ])
      )
      this.camera.lookAt(0, 0, 0)
      this.camera.position.add(new Vector3(...gui.translation))
      this.camera.updateMatrix()
      this.camera.updateProjectionMatrix()

      this.renderer.render(this.scene, this.camera)
      return this.canvas.toBuffer() as Buffer
   }

   private modelPath({ id, mod }: Named, type: string) {
      const path = id.includes('/') ? id : `${type}/${id}`
      return join(this.dir, mod ?? 'minecraft', 'models', `${path}.json`)
   }

   private keyFrom(key: string): Named {
      if (!key.includes(':')) return { mod: 'minecraft', id: key }
      const [mod, id] = key.split(':')
      return { mod, id }
   }

   getModel(block: Named, type: string): BlockModel {
      const path = this.modelPath(block, type)
      if (!existsSync(path)) throw new Error('Model missing')
      const raw = readFileSync(path).toString()
      const parsed = JSON.parse(raw) as BlockModel

      if (parsed.parent) {
         if (parsed.parent.includes('builtin')) merge(parsed, BUILTIN)
         else merge(parsed, this.getModel(this.keyFrom(parsed.parent), type))
      }

      return parsed
   }

   async getTexture({ mod, id }: Named) {
      const path = join(this.dir, mod ?? 'minecraft', 'textures', `${id}.png`)
      return readFileSync(path)
   }

   async getMetadata({ mod, id }: Named) {
      const path = join(this.dir, mod ?? 'minecraft', 'textures', `${id}.png.mcmeta`)
      if (!existsSync(path)) return null
      const raw = readFileSync(path).toString()
      return JSON.parse(raw) as AnimationMeta
   }

   private async decodeFace(face: Face, block: BlockModel): Promise<Material | null> {
      const decodedTexture = this.decodeTexture(face.texture, block)
      if (!decodedTexture) return null
      return this.constructTextureMaterial(block, decodedTexture, face)
   }

   private async constructBlockMaterial(block: BlockModel, element: Element): Promise<Material[]> {
      if (!element?.faces) return []
      const materials = await Promise.all(
         FACES.map(direction => {
            const face = element?.faces?.[direction]
            if (!face) return null
            return this.decodeFace(face, block)
         })
      )
      return materials.filter(it => !!it) as Material[]
   }

   private decodeTexture(texture: string, block: BlockModel): string | null {
      if (!texture) return null
      if (!texture.startsWith('#')) return texture

      const correctedTextureName = block.textures?.[texture.substring(1) as BlockSides]
      if (!correctedTextureName) return null

      return this.decodeTexture(correctedTextureName, block)
   }

   async constructTextureMaterial(block: BlockModel, path: string, face: Face) {
      const image = await loadImage(await this.getTexture(this.keyFrom(path)))
      const animationMeta = await this.getMetadata(this.keyFrom(path))

      const width = image.width
      let height = animationMeta ? width : image.height
      let frame = 0

      if (animationMeta) {
         const frameCount = image.height / width

         if (block.animationCurrentTick == 0) {
            block.animationMaxTicks = Math.max(
               block.animationMaxTicks || 1,
               frameCount * (animationMeta.frametime || 1)
            )
         } else {
            frame =
               Math.floor(block.animationCurrentTick! / (animationMeta.frametime || 1)) % frameCount
         }
      }

      const canvas = rawCanvas.createCanvas(width, height)
      const ctx = canvas.getContext('2d')

      ctx.imageSmoothingEnabled = false

      if (face.rotation) {
         ctx.translate(width / 2, height / 2)
         ctx.rotate(face.rotation * MathUtils.DEG2RAD)
         ctx.translate(-width / 2, -height / 2)
      }

      const uv = face.uv ?? [0, 0, width, height]

      ctx.drawImage(
         image,
         uv[0],
         uv[1] + frame * height,
         uv[2] - uv[0],
         uv[3] - uv[1],
         0,
         0,
         width,
         height
      )

      const texture = new Texture(canvas as any)
      texture.magFilter = NearestFilter
      texture.minFilter = NearestFilter
      texture.needsUpdate = true

      return new MeshBasicMaterial({
         map: texture,
         //color: 0xffffff,
         transparent: true,
         //roughness: 1,
         //metalness: 0,
         //emissive: 1,
         alphaTest: 0.1,
      })
   }
}
