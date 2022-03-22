import {Component} from "react";
import {Route, BrowserRouter as Router, Routes} from "react-router-dom";
import LoginComponent from "./login/LoginComponent";
import App from "./App";
import StudentComponent from "./students/StudentComponent";
import CourseComponent from "./courses/CourseComponent";

class InitialComponent extends Component {

    render() {
        return (
            <div>
                <Router>
                    <Routes>
                        <Route path="/" exact element={<LoginComponent />}></Route>
                        <Route path="/login" element={<LoginComponent />}></Route>
                        <Route path="/student" element={<App component={<StudentComponent />} />}></Route>
                        <Route path="/course" element={<App component={<CourseComponent />} />}></Route>
                    </Routes>
                </Router>
            </div>
        )
    }
}

export default InitialComponent;