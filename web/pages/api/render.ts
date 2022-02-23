import { NextApiHandler } from 'next'
import createRenders from '../../util/data/renderer'

const handler: NextApiHandler = async (req, res) => {
   const result = await createRenders()
   res.json(result)
}

export default handler
