import { NextApiHandler } from 'next'
import serverParser from 'util/parser/serverParser'

const handle: NextApiHandler = async (req, res) => {
   const icon = await serverParser.getIcon(req.body)
   res.json({ icon })
}

export default handle
