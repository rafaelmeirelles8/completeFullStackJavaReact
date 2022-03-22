import './App.css';
import {deleteStudent, getAllStudents} from './client';
import { useEffect, useState } from 'react';
import {
  Layout,
  Menu,
  Breadcrumb,
  Table,
  Spin,
  Button,
  Tag,
  Avatar,
  Popconfirm,
  Radio,
  message,
  Image,
  Divider
} from 'antd';
import {
  UserOutlined,
  LoadingOutlined,
  UserAddOutlined, ReadOutlined, LogoutOutlined,
} from '@ant-design/icons';
import StudentDrawerForm from "./StudentDrawerForm";
import StudentsCountBadge from "./StudentsCountBadge";
import {errorNotification, successNotification} from "./Notification";
import {useNavigate} from "react-router-dom";

const { Header, Content, Footer, Sider } = Layout;

const TheAvatar = ({name}) => {
  let nameTrimmed = name.trim();
  if(nameTrimmed.length === 0) {
    return <Avatar icon={<UserOutlined />} />;
  }

  let nameSplitted = nameTrimmed.split(" ");
  if(nameSplitted.length === 1) {
    return <Avatar>{nameSplitted[0].charAt(0)}</Avatar>
  }

  return <Avatar>{`${nameSplitted[0].charAt(0)}${nameSplitted[1].charAt(0)}`}</Avatar>
}

function removeStudent(student, fetchStudents) {
  deleteStudent(student)
      .then(() => {
        successNotification('Student deleted successfully',
            `Student with id ${student.id} was deleted!`);
        fetchStudents();
      })
      .catch(err => {
        err.response.json().then(res => {
          errorNotification("Student not deleted!",
              `${res.message} [${res.status}]`)
          //message.error(`Student with id: ${student.id} was not deleted!`);
        })
      });
}

function cancel(e) {
  message.error('Click on No');
}

const antIcon = <LoadingOutlined style={{ fontSize: 24 }} spin />;

function App() {

  const columns = fetchStudents => [
    {
      title: '',
      dataIndex: 'avatar',
      key: 'avatar',
      render: (text, student) => {
        return <TheAvatar name={student.name} ></TheAvatar>
      }
    },
    {
      title: 'Id',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: 'Gender',
      dataIndex: 'gender',
      key: 'gender',
    },
    {
      title: 'Actions',
      dataIndex: 'actions',
      key: 'actions',
      render: (text, student) =>
          <Radio.Group>
            <Popconfirm
                placement="topRight"
                title={`Are you sure to delete student ${student.name}?`}
                onConfirm={() => removeStudent(student, fetchStudents)}
                onCancel={cancel}
                okText="Yes"
                cancelText="No"
            >
              <Button value="small">Delete</Button>
            </Popconfirm>
            <Radio.Button value="small" onClick={() => editStudent(student.id)}>Edit</Radio.Button>
          </Radio.Group>
    },
  ];

  const [students, setStudents] = useState([]);
  const [collapsed, setCollapsed] = useState(false);
  const [fetching, setFetching] = useState(true);
  const [showDrawer, setShowDrawer] = useState(false);
  const [loggedUser, setLoggedUser] = useState('');
  const [studentToEdit, setStudentToEdit] = useState(-1);
  const navigate = useNavigate();

  function editStudent(studentId) {
    setShowDrawer(!showDrawer);
    setStudentToEdit(studentId);
  }

  const fetchStudents = () =>
      getAllStudents()
          .then(res => res.json())
          .then(data => {
            setStudents(data);
          })
          .catch(err => {
            err.response.json().then(res => errorNotification("There was an issue", `${res.message} [statusCode:${res.status}] [${res.error}]`));
          })
          .finally(() => {
            setFetching(false);
          })

  useEffect(() => {
    setLoggedUser('Rafael');
    fetchStudents();
  }, []);

  const renderStudents = () => {
    if(fetching) {
      return <Spin indicator={antIcon} />
    }

    /*if(students.length <= 0) {
      return <Empty />
    }*/

    return <>
      <StudentDrawerForm
          showDrawer={showDrawer}
          setShowDrawer={setShowDrawer}
          fetchStudents={fetchStudents}
          studentToEditID={studentToEdit}
      />
      <Table dataSource={students}
             rowKey={(student) => student.id}
             columns={columns(fetchStudents)}
             bordered
             title={
               () =>
                   <>
                     <Tag>Number of students</Tag>
                     <StudentsCountBadge count={students.length}/>
                     <p></p>
                     <Button
                         onClick={() => editStudent(-1)}
                         type="primary" shape="round" icon={<UserAddOutlined />} size="small" >
                       Add Student
                     </Button>
                   </>
             }
             pagination={{ pageSize: 50 }}
             scroll={{ y: 500 }}/>
    </>
  }

  function logout() {
    navigate('/login');

    successNotification('Logout successfully',
        `User has been logged out`);
  }

  return <Layout style={{ minHeight: '100vh' }}>
    <Sider collapsible collapsed={collapsed}
           onCollapse={setCollapsed}>
      <div className="logo" />
      <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline">
        <Menu.Item key="1" icon={<UserOutlined />}>
          Students
        </Menu.Item>
        <Menu.Item key="2" icon={<ReadOutlined />}>
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
          {renderStudents()}
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
