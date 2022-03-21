import { mix } from 'polished'
import { ReactNode, useCallback, useMemo } from 'react'
import ReactSelect, {
   ActionMeta,
   MultiValue,
   OnChangeValue,
   Props,
   StylesConfig,
   ThemeConfig,
} from 'react-select'
import { useTheme } from 'styled-components'
import Labeled from './Labeled'

function useStyles() {
   const { bg, text, accent } = useTheme()

   const theme: ThemeConfig = ({ colors, ...base }) => {
      function mapColors(key: string, map: (v: number) => string) {
         const entries = Object.keys(colors)
            .filter(it => it.startsWith(key))
            .map(it => {
               const float = Number.parseFloat(it.substring(key.length))
               return [it, map(isNaN(float) ? 1 : float / 100)]
            })
         return Object.fromEntries(entries)
      }

      const neutrals = mapColors('neutral', v => mix(v, text, bg))
      const primary = mapColors('primary', v => mix(v, accent, bg))

      return {
         ...base,
         borderRadius: 0,
         colors: { ...colors, ...neutrals, ...primary },
      }
   }

   const styles: StylesConfig = {
      input: base => ({ ...base, color: text }),
      container: base => ({ ...base, minWidth: 300, maxWidth: 700 }),
   }

   return { theme, styles }
}

interface Option {
   label: string
   value: string
}

function Dropdown<Multi extends boolean>({
   label,
   ...props
}: Props<Option, Multi> &
   Required<Pick<Props<Option, Multi>, 'id' | 'options'>> & {
      label: ReactNode
   }) {
   const styles = useStyles()

   const options = useMemo(
      () =>
         props.isMulti
            ? [{ label: 'Select All', value: 'ALL' }, ...(props.options ?? [])]
            : props.options,
      [props.isMulti, props.options]
   )

   const onChange = useCallback(
      (value: OnChangeValue<Option, Multi>, meta: ActionMeta<Option>) => {
         if (!props.isMulti) return props.onChange?.(value, meta)
         const values = (value as MultiValue<Option>).some(it => it.value === 'ALL')
            ? (props.options as OnChangeValue<Option, Multi>)
            : value
         return props.onChange?.(values, meta)
      },
      [props]
   )

   return (
      <Labeled prefix htmlFor={props.id} label={label}>
         {/*@ts-ignore */}
         <ReactSelect {...props} options={options} {...styles} onChange={onChange} />
      </Labeled>
   )
}

export default Dropdown
