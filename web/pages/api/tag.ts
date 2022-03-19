import { NextApiHandler } from 'next'
import serverParser from 'util/parser/serverParser'

const handle: NextApiHandler = async (req, res) => {
   const matches = await serverParser.getBlocksFor(req.body)
   res.json(matches)
}

export default handle
