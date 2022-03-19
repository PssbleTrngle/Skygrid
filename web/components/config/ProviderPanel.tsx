import { createElement, DispatchWithoutAction, FC, VFC } from 'react'
import styled, { css } from 'styled-components'
import { BlockProvider, BlockProviders, ProviderType } from 'util/parser/types/BlockProviders'
import { forPolymorph } from '../../util/polymorphism'
import Panel from './Panel'
import BlockPanel from './providers/BlockPanel'
import ListPanel from './providers/ListPanel'
import ReferencePanel from './providers/ReferencePanel'
import TagPanel from './providers/TagPanel'

const UnknownProvider: VFC<BlockProvider> = ({ type }) => <span title={type}>Unkown Type</span>

export const panelComponent = (provider: BlockProvider) =>
   forPolymorph<BlockProviders, FC<any>>(provider, {
      block: () => BlockPanel,
      tag: () => TagPanel,
      list: () => ListPanel,
      reference: () => ReferencePanel,
   }) ?? UnknownProvider

const ProviderPanel: FC<{
   provider: BlockProvider
   onClick?: DispatchWithoutAction
   size: number
}> = ({ provider, size, children, ...props }) => {
   const component = panelComponent(provider)

   return (
      <Style {...props} reference={provider.type === ProviderType.REFERENCE} size={size}>
         {createElement(component, { ...provider, size })}
         <p key='weight'>{(provider.weight * 100).toFixed(2)}%</p>
         {children}
      </Style>
   )
}

const Style = styled(Panel)<{ size: number; reference?: boolean }>`
   grid-template-rows: ${p => p.size}px repeat(auto-fit, 1.2em);
   ${p =>
      p.reference &&
      css`
         border: ${p.theme.accent} 1px solid;
      `}
`

export default ProviderPanel
