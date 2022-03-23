import {Avatar, Button, message, Popconfirm, Radio, Spin, Table, Tag} from "antd";
import CountBadge from "../Common/CountBadge";
import {LoadingOutlined, UserAddOutlined, UserOutlined} from "@ant-design/icons";
import {errorNotification, successNotification} from "../Notification";
import {useEffect, useState} from "react";
import CourseDrawerForm from "./CourseDrawerForm";
import {deleteCourse, getAllCourses} from "./CourseService";


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

function removeCourse(course, fetchCourses) {
    deleteCourse(course)
        .then(() => {
            successNotification('Course deleted successfully',
                `Course with id ${course.id} was deleted!`);
            fetchCourses();
        })
        .catch(err => {
            err.response.json().then(res => {
                errorNotification("Course not deleted!",
                    `${res.message} [${res.status}]`)
                //message.error(`Course with id: ${course.id} was not deleted!`);
            })
        });
}

function CourseComponent() {

    const [courses, setCourses] = useState([]);
    const [fetching, setFetching] = useState(true);
    const [showDrawer, setShowDrawer] = useState(false);
    const [courseToEdit, setCourseToEdit] = useState(-1);

    const columns = fetchCourses => [
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
            render: (text, course) =>
                <Radio.Group>
                    <Popconfirm
                        placement="topRight"
                        title={`Are you sure to delete course ${course.name}?`}
                        onConfirm={() => removeCourse(course, fetchCourses)}
                        onCancel={cancel}
                        okText="Yes"
                        cancelText="No"
                    >
                        <Button value="small">Delete</Button>
                    </Popconfirm>
                    <Radio.Button value="small" onClick={() => editCourse(course.id)}>Edit</Radio.Button>
                </Radio.Group>
        },
    ];

    function editCourse(courseId) {
        setShowDrawer(!showDrawer);
        setCourseToEdit(courseId);
    }

    useEffect(() => {
        console.log('course');
        fetchCourses();
    }, []);

    const fetchCourses = () =>
        getAllCourses()
            .then(res => res.json())
            .then(data => {
                setCourses(data);
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

        /*if(courses.length <= 0) {
          return <Empty />
        }*/

        return <>
            <CourseDrawerForm
                showDrawer={showDrawer}
                setShowDrawer={setShowDrawer}
                fetchCourses={fetchCourses}
                courseToEditID={courseToEdit}
            />
            <Table dataSource={courses}
                   rowKey={(course) => course.id}
                   columns={columns(fetchCourses)}
                   bordered
                   title={
                       () =>
                           <>
                               <Tag>Number of courses</Tag>
                               <CountBadge count={courses.length}/>
                               <p></p>
                               <Button
                                   onClick={() => editCourse(-1)}
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