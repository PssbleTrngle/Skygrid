import { createElement, DispatchWithoutAction, FC, memo, VFC } from 'react'
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
      fallback: () => ListPanel,
      reference: () => ReferencePanel,
   }) ?? UnknownProvider

const ProviderPanel: VFC<{
   provider: BlockProvider & { occurenced?: number }
   onClick?: DispatchWithoutAction
   size: number
}> = ({ provider, size, ...props }) => {
   const component = panelComponent(provider)

   return (
      <Style {...props} reference={provider.type === ProviderType.REFERENCE} size={size}>
         {createElement(component, { ...provider, size })}
         <p key='weight'>{(provider.weight * 100).toFixed(2)}%</p>
         <p>
            {/*TODO DUMB PLURAL*/}
            {provider.occurenced} entr{provider.occurenced === 1 ? 'y' : 'ies'}
         </p>
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

export default memo(ProviderPanel)
