import { createElement, DispatchWithoutAction, FC } from 'react'
import { BlockList, BlockProvider, BlockProviders } from '../../types/BlockProviders'
import { forPolymorph } from '../../util/polymorphism'
import BlockPanel from './BlockPanel'
import Panel from './Panel'
import TagPanel from './TagPanel'

const ListPanel: FC<BlockList> = p => (
   <>
      <p>{p.name ?? 'list'}</p>
      <p>({p.children.length} entries)</p>
   </>
)

const ProviderPanel: FC<{
   provider: BlockProvider
   onClick?: DispatchWithoutAction
   size: number
}> = ({ provider, size, children, ...props }) => {
   const component = forPolymorph<BlockProviders, FC<any>>(provider, {
      block: () => BlockPanel,
      tag: () => TagPanel,
      list: () => ListPanel,
   })

   return (
      <Panel {...props} size={size}>
         {component ? (
            createElement(component, { ...provider, size }, [
               <p key='weight'>{(provider.weight * 100).toFixed(2)}%</p>,
               children,
            ])
         ) : (
            <span>Unknown Type</span>
         )}
      </Panel>
   )
}

export default ProviderPanel
