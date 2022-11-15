import Navbar from "./Navbar"
import Home from "./pages/Home"
import Auth from "./pages/Auth"
import { Route, Routes } from "react-router-dom"
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css"

function App() {
  return (
    <>
      <Navbar />
      <div className="container">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/auth" element={<Auth />} />
        </Routes>
      </div>
    </>
  )
}

export default App