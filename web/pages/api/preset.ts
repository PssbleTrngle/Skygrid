import { NextApiHandler } from 'next'
import serverParser from '../../util/data/serverParser'

const handle: NextApiHandler = async (req, res) => {
   const preset = await serverParser.getPreset(req.body)
   res.json(preset)
}

export default handle
