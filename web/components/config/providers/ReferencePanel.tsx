import { createElement, VFC } from 'react'
import { Reference } from '../../../@types/BlockProviders'
import { panelComponent } from '../ProviderPanel'

const ReferencePanel: VFC<Reference & { size: number }> = ({ provider, ...props }) => {
   return createElement(panelComponent(provider), {
      ...props,
      ...provider,
      name: provider.name ?? props.id,
   })
}

export default ReferencePanel
