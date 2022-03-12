import { NextPage } from 'next'
import Page from '../../components/basic/Page'
import useLocalFiles from '../../hooks/useLocalFiles'

const Local: NextPage = () => {
   const { open } = useLocalFiles()

   return (
      <Page>
         <button onClick={open}>
            Select <code>data</code> Folder
         </button>
      </Page>
   )
}

export default Local
