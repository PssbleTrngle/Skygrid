import { DispatchWithoutAction, VFC } from 'react'
import { BlockProviders, TypedProvider } from '../../types/BlockProviders'
import { forPolymorph } from '../../util/polymorphism'
import BlockPanel from './BlockPanel'
import Panel from './Panel'

const ProviderPanel: VFC<{ provider: TypedProvider; onClick?: DispatchWithoutAction }> = ({ provider, ...props }) => {
   return (
      <div {...props}>
         {forPolymorph<BlockProviders, ReturnType<VFC>>(provider, {
            block: p => <BlockPanel size={100} block={p} />,
            tag: p => <BlockPanel size={100} block={p} />,
            list: p => (
               <Panel>
                  <p>{p.name ?? 'list'}</p>
                  <p>({p.children.length} entries)</p>
                  <p>{(p.weight * 100).toFixed(2)}%</p>
               </Panel>
            ),
         }) ?? <Panel title={provider.type}>Unknown Type</Panel>}
      </div>
   )
}

export default ProviderPanel
