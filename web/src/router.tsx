import { RouteObject } from "react-router-dom";
import Home from "./pages/Home";

const routes: RouteObject[] = [
   { path: '/', element: <Home /> },
   { path: '*', element: <p>404 - Not Found</p> },
]

export default routes