import type { Canvas } from 'canvas'
import 'node-canvas-webgl'

declare module 'node-canvas-webgl' {
   function createCanvas(): Canvas & {
      __ctx__: unknown
   }
}
