import { NextApiHandler } from 'next'
import serverParser from '../../util/data/serverParser'

const handle: NextApiHandler = async (req, res) => {
   const icon = await serverParser.getIcon(req.body)
   res.json({ icon })
}

export default handle
