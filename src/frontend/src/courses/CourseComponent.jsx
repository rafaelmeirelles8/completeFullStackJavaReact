import {Avatar, Button, message, Popconfirm, Radio, Spin, Table, Tag} from "antd";
import StudentsCountBadge from "../StudentsCountBadge";
import {LoadingOutlined, UserAddOutlined, UserOutlined} from "@ant-design/icons";
import {deleteStudent, getAllStudents} from "../client";
import {errorNotification, successNotification} from "../Notification";
import {useEffect, useState} from "react";
import CourseDrawerForm from "./CourseDrawerForm";


const antIcon = <LoadingOutlined style={{ fontSize: 24 }} spin />;

function cancel(e) {
    message.error('Click on No');
}

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

function CourseComponent() {

    const [students, setStudents] = useState([]);
    const [fetching, setFetching] = useState(true);
    const [showDrawer, setShowDrawer] = useState(false);
    const [studentToEdit, setStudentToEdit] = useState(-1);

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
            title: 'Code',
            dataIndex: 'code',
            key: 'code',
        },
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
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

    function editStudent(studentId) {
        setShowDrawer(!showDrawer);
        setStudentToEdit(studentId);
    }

    useEffect(() => {
        console.log('course');
        fetchStudents();
    }, []);

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

    const renderCourses = () => {
        if(fetching) {
            return <Spin indicator={antIcon} />
        }

        /*if(students.length <= 0) {
          return <Empty />
        }*/

        return <>
            <CourseDrawerForm
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
                               <Tag>Number of courses</Tag>
                               <StudentsCountBadge count={students.length}/>
                               <p></p>
                               <Button
                                   onClick={() => editStudent(-1)}
                                   type="primary" shape="round" icon={<UserAddOutlined />} size="small" >
                                   Add Course
                               </Button>
                           </>
                   }
                   pagination={{ pageSize: 50 }}
                   scroll={{ y: 500 }}/>
        </>
    }

    return (
        <div>
            {renderCourses()}
        </div>
    );
}

export default CourseComponent;