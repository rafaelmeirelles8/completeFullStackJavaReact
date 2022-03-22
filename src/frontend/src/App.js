import './App.css';
import {useEffect, useState} from 'react';
import {Breadcrumb, Button, Divider, Image, Layout, Menu} from 'antd';
import {LogoutOutlined, ReadOutlined, UserOutlined,} from '@ant-design/icons';
import {successNotification} from "./Notification";
import {Link, useNavigate} from "react-router-dom";
import StudentComponent from "./students/StudentComponent";
import CourseComponent from "./courses/CourseComponent";

const { Header, Content, Footer, Sider } = Layout;

function App(props) {

  const [collapsed, setCollapsed] = useState(false);
  const [loggedUser, setLoggedUser] = useState('');
  const navigate = useNavigate();
  const [component, setComponent] = useState(props.component);

  useEffect(() => {
    setLoggedUser('Rafael');
  }, []);

  function logout() {
    navigate('/login');

    successNotification('Logout successfully',
        `User has been logged out`);
  }

  function nav() {
    navigate('/student');
  }

  return <Layout style={{ minHeight: '100vh' }}>
              <Sider collapsible collapsed={collapsed}
                     onCollapse={setCollapsed}>
                <div className="logo" />
                <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline">
                  <Menu.Item key="1" icon={<UserOutlined />} onClick={() => setComponent(<StudentComponent />)}>
                    Students
                  </Menu.Item>
                  <Menu.Item key="2" icon={<ReadOutlined />} onClick={() => setComponent(<CourseComponent />)}>
                    Courses
                  </Menu.Item>
                </Menu>
              </Sider>
              <Layout className="site-layout">
                <Header className="site-layout-background" style={{ padding: 15 }} >
                  <Button
                  onClick={logout}
                      style={{float: 'right'}}
                      type="primary" icon={<LogoutOutlined />}>
                    Logout
                  </Button>
                </Header>
                <Content style={{ margin: '0 16px' }}>
                  <Breadcrumb style={{ margin: '16px 0' }}>
                    <Breadcrumb.Item>Logged User</Breadcrumb.Item>
                    <Breadcrumb.Item>{loggedUser}</Breadcrumb.Item>
                  </Breadcrumb>
                  <div className="site-layout-background" style={{ padding: 24, minHeight: 360 }}>
                    {component}
                  </div>
                </Content>
                <Footer style={{ textAlign: 'center' }}>
                  <Image
                      width={75}
                      src="https://user-images.githubusercontent.com/56368908/156858168-f36a5881-7f4f-4c88-adcc-c80728666f90.jpg"
                  />
                  <p>Rafael's FullStack Java+React+aws</p>
                  <Divider/>
                  <a rel="noopener noreferrer"
                     target="_blank"
                     href="https://www.linkedin.com/in/rafael-meirelles-b644979a/">My Linkedin
                  </a>
                  <br/>
                  <a rel="noopener noreferrer"
                     target="_blank"
                     href="https://github.com/rafaelmeirelles8/">My Github
                  </a>
                </Footer>
              </Layout>
        </Layout>
}

export default App;
