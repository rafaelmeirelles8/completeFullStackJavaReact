import {Component} from "react";
import {Route, BrowserRouter as Router, Routes} from "react-router-dom";
import LoginComponent from "./login/LoginComponent";
import App from "./App";

class InitialComponent extends Component {

    render() {
        return (
            <div>
                <Router>
                    <Routes>
                        <Route path="/" exact element={<LoginComponent />}></Route>
                        <Route path="/login" element={<LoginComponent />}></Route>
                        <Route path="/main" element={<App />}></Route>
                    </Routes>
                </Router>
            </div>
        )
    }
}

export default InitialComponent;