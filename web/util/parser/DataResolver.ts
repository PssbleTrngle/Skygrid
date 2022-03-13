import { Block } from '../../@types/BlockProviders'

export default interface DataResolver {
   exists(...path: string[]): Promise<boolean>
   getContent(...path: string[]): Promise<string | null>
   getName(block: Block): Promise<string | null>
   list(...path: string[]): Promise<string[]>
}
