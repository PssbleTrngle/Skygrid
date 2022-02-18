import { useRoutes } from 'react-router-dom'
import routes from './router'

function App() {
   const element = useRoutes(routes)

   return (
      <>
         <section>{element}</section>
      </>
   )
}

export default App
