import {useNavigate} from "react-router-dom";
import {errorNotification, successNotification} from "../Notification";
import { Form, Input, Button, Checkbox } from 'antd';
import './LoginComponent.css';
import {ClearOutlined, LoginOutlined} from "@ant-design/icons";

function LoginComponent() {

    const navigate = useNavigate();

    const onFinish = (values) => {
        navigate('/main');

        successNotification('Login successfully',
            `User has been logged in`);
    };

    const onFinishFailed = (errorInfo) => {
        errorNotification('Failure to login',
            `User has not been logged in`);
    };

    return (
        <div className="div">
            <h1 className="h1">Login</h1>
            <Form
                name="basic"
                initialValues={{ remember: true }}
                onFinish={onFinish}
                onFinishFailed={onFinishFailed}
                autoComplete="off"
            >
                <Form.Item
                    label="Username"
                    name="username"
                    rules={[{ required: true, message: 'Please input your username!' }]}
                >
                    <Input />
                </Form.Item>

                <Form.Item
                    label="Password"
                    name="password"
                    rules={[{ required: true, message: 'Please input your password!' }]}
                >
                    <Input.Password />
                </Form.Item>

                <Form.Item name="remember" valuePropName="checked" >
                    <Checkbox>Remember me</Checkbox>
                </Form.Item>

                <Form.Item >
                    <Button icon={<LoginOutlined />} type="primary" htmlType="submit">
                        Login
                    </Button>
                </Form.Item>
                <Form.Item >
                    <Button icon={<ClearOutlined />} type="primary" htmlType="reset">
                        Clear
                    </Button>
                </Form.Item>
            </Form>
        </div>
    );
}

export default LoginComponent;