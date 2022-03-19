import { NextApiHandler } from 'next'
import serverParser from 'util/parser/serverParser'

const handle: NextApiHandler = async (req, res) => {
   const preset = await serverParser.getPreset(req.body)
   res.json(preset)
}

export default handle
