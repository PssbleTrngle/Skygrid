import dotenv from 'dotenv'
import yargs from 'yargs'
import { hideBin } from 'yargs/helpers'

dotenv.config()

const getOptions = async () =>
   yargs(hideBin(process.argv))
      .demandOption('from')
      .string('data')
      .string('icons')
      .string('lang')
      .string('temp')
      .default('temp', 'tmp')
      .boolean('cached')
      .default('cached', true)
      .parse() as Options

export type Options = Readonly<{
   from: string
   data?: string
   icons?: string
   lang?: string
   temp: string
   cached: boolean
}>

export default getOptions
