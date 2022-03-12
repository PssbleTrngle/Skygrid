import { BlockProvider } from './BlockProviders'

export default interface Preset {
   children: [BlockProvider]
}