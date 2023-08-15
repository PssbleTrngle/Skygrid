import Dropdown from 'components/inputs/Dropdown'
import { useRouter } from 'next/router'
import { useCallback, useEffect, useMemo, VFC } from 'react'
import styled from 'styled-components'
import { Named } from 'util/parser/types'
import DimensionConfig from 'util/parser/types/DimensionConfig'
import ActionBar from './ActionBar'
import HierarchicalBlocks from './HierachicalBlocks'
import UnwrappedBlocks from './UnwrappedBlocks'

export enum View {
   HIERACHICAL = 'hierachical',
   UNWRAPPED = 'unwrapped',
}

const ConfigVisualizer: VFC<{
   config: DimensionConfig
   options?: Named[]
}> = ({ config, options, ...props }) => {
   const { query, replace, push } = useRouter()

   const view = useMemo(() => (query.view as View) ?? View.HIERACHICAL, [query])
   const setView = useCallback(
      (v: View) => replace({ query: { ...query, view: v } }),
      [query, replace]
   )

   useEffect(() => {
      if (!Object.values(View).includes(view)) {
         setView(View.HIERACHICAL)
      }
   }, [setView, view])

   const labelledOptions = useMemo(
      () => options?.map(value => ({ value, label: `${value.mod}:${value.id}` })),
      [options]
   )

   return (
      <Style {...props}>
         <ActionBar view={view} onView={setView}>
            {labelledOptions && (
               <Dropdown
                  id='skygrid-config'
                  label=''
                  options={labelledOptions}
                  onChange={it =>
                     push({
                        query: { ...query, namespace: it?.value.mod, config: it?.value.id },
                     })
                  }
                  value={labelledOptions.find(
                     it => it.value.mod === query.namespace && it.value.id == query.config
                  )}
               />
            )}
         </ActionBar>
         {view === View.HIERACHICAL && <HierarchicalBlocks blocks={config.blocks.children} />}
         {view === View.UNWRAPPED && <UnwrappedBlocks blocks={config.blocks.children} />}
      </Style>
   )
}

const Style = styled.div``

export default ConfigVisualizer
