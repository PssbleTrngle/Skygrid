import { orderBy, sumBy } from 'lodash'
import { useMemo, VFC } from 'react'
import {
   BlockProviders,
   GeneratedBlock,
   ProviderType,
   TypedProvider
} from '../../types/BlockProviders'
import { forPolymorph } from '../../util/polymorphism'
import BlockGrid from './BlockGrid'
import ProviderPanel from './ProviderPanel'

const UnwrappedBlocks: VFC<{ blocks: TypedProvider[] }> = ({ blocks }) => {
   const unwrapped = useMemo(() => unwrap(blocks), [blocks])
   const sorted = useMemo(() => orderBy(unwrapped, b => b.weight, 'desc'), [unwrapped])
   const size = 100

   return (
      <BlockGrid size={size}>
         {sorted.map(block => (
            <ProviderPanel
               key={block.uuid}
               size={size}
               provider={{ ...block, type: ProviderType.BLOCK }}
            />
         ))}
      </BlockGrid>
   )
}

function withWeight(
   blocks: GeneratedBlock[],
   func: (w: number) => number = w => w
): GeneratedBlock[] {
   return blocks.map(b => ({ ...b, weight: func(b.weight) }))
}

function unwrapProvider(provider: TypedProvider): GeneratedBlock[] {
   return (
      forPolymorph<BlockProviders, GeneratedBlock[]>(provider, {
         block: p => withWeight([p]),
         tag: p => withWeight(p.matches, w => w / p.matches.length),
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
