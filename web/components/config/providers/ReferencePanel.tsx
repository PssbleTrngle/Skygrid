import { createElement, VFC } from 'react'
import { Reference } from '../../../@types/BlockProviders'
import { panelComponent } from '../ProviderPanel'

const ReferencePanel: VFC<Reference & { size: number }> = ({ provider, ...props }) =>
   createElement(panelComponent(provider), { ...props, ...provider })

export default ReferencePanel
