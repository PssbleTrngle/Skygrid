import { orderBy, sumBy } from 'lodash'
import { useMemo, VFC } from 'react'
import { Block, BlockProviders, GeneratedBlock, Tag, TypedProvider } from '../../types/BlockProviders'
import { forPolymorph } from '../../util/polymorphism'
import BlockGrid from './BlockGrid'
import BlockPanel from './BlockPanel'

const UnwrappedBlocks: VFC<{ blocks: TypedProvider[] }> = ({ blocks }) => {
   const unwrapped = useMemo(() => unwrap(blocks), [blocks])
   const sorted = useMemo(() => orderBy(unwrapped, b => b.weight, 'desc'), [unwrapped])

   return (
      <BlockGrid>
         {sorted.map((block, ) => (
            <BlockPanel key={block.uuid} block={block} size={100} />
         ))}
      </BlockGrid>
   )
}

function withWeight(blocks: GeneratedBlock[], func: (w: number) => number = w => w): GeneratedBlock[] {
   return blocks.map(b => ({ ...b, weight: func(b.weight) }))
}

function blocksOfTag(tag: Tag): Block[] {
   const id = tag.id.startsWith('#') ? tag.id.substring(1) : tag.id
   return [{ id, mod: tag.mod, weight: 1, uuid: tag.uuid }]
}

function unwrapProvider(provider: TypedProvider): GeneratedBlock[] {
   return (
      forPolymorph<BlockProviders, GeneratedBlock[]>(provider, {
         block: p => withWeight([p]),
         tag: p => {
            const blocks = blocksOfTag(p)
            return withWeight(blocks, w => w / blocks.length)
         },
         list: p => withWeight(unwrap(p.children), w => w * p.weight),
         fallback: p => withWeight(unwrap(p.children).slice(0), () => p.weight),
      }) ?? []
   )
}

function unwrap(providers: TypedProvider[]): GeneratedBlock[] {
   const unwrapped = providers.flatMap(p => unwrapProvider(p))
   const total = sumBy(unwrapped, w => w.weight)
   return unwrapped.map((p, _, a) => ({ ...p, weight: p.weight / total }))
}

export default UnwrappedBlocks
