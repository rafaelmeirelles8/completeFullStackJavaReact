import {Button, Col, Drawer, Form, Input, Row, Spin} from 'antd';
import {LoadingOutlined} from "@ant-design/icons";
import {useEffect, useState} from "react";
import {errorNotification, successNotification} from "../Notification";
import {createCourse, getCourseById, updateCourse} from "./CourseService";

const antIcon = <LoadingOutlined style={{ fontSize: 24 }} spin />;

function CourseDrawerForm({showDrawer, setShowDrawer, fetchCourses, courseToEditID}) {
    const onCLose = () => {
        setShowDrawer(false);
        form.resetFields();
    }
    const [submitting, setSubmitting] = useState(false);
    const [form] = Form.useForm();

    useEffect(() => {
        if(showDrawer && courseToEditID > 0) {
            getCourseById(courseToEditID)
                .then((res) => res.json())
                .then(data => {
                    form.setFieldsValue({ name: data.name });
                    form.setFieldsValue({ email: data.email });
                    form.setFieldsValue({ gender: data.gender });
                })
                .catch(() => {
                    //err.response.json().then(res => {
                    //    errorNotification('Course not found', `${res.message} [${res.status}]`);
                    //})
                });
        }
    });

    const onFinish = course => {
        setSubmitting(true);

        if(courseToEditID <= 0) {
            createCourse(course)
                .then(() => {
                    successNotification('Course created successfully',
                        `${course.name} was added to the system`);
                    onCLose();
                    fetchCourses();
                })
                .catch(err => {
                    err.response.json().then(res => {
                        errorNotification('Course was not created', `${res.message} [${res.status}]`);
                    })

                })
                .finally(() => setSubmitting(false));
        }
        else {
            course.id = courseToEditID;
            updateCourse(course)
                .then(() => {
                    successNotification('Course updated successfully',
                        `${course.name} was updated to the system`);
                    onCLose();
                    fetchCourses();
                })
                .catch(err => {
                    err.response.json().then(res => {
                        errorNotification('Course was not updated', `${res.message} [${res.status}]`);
                    })

                })
                .finally(() => setSubmitting(false));

        }
    };


    const onFinishFailed = errorInfo => {
        alert(JSON.stringify(errorInfo, null, 2));
    };

    const Demo = () => {

        return <Drawer
            title="Create new course"
            width={720}
            onClose={onCLose}
            visible={showDrawer}
            bodyStyle={{paddingBottom: 80}}

            destroyOnClose="true"
            footer={
                <div
                    style={{
                        textAlign: 'right',
                    }}
                >
                    <Button onClick={onCLose} style={{marginRight: 8}}>
                        Cancel
                    </Button>
                </div>
            }
        >
            <Form layout="vertical"
                  form={form}
                  onFinishFailed={onFinishFailed}
                  onFinish={onFinish}
                  hideRequiredMark>
                <Row gutter={16}>
                    <Col span={12}>
                        <Form.Item
                            name="name"
                            label="Name"
                            rules={[{required: true, message: 'Please enter course name'}]}
                        >
                            <Input placeholder="Please enter course name"/>
                        </Form.Item>
                    </Col>
                    <Col span={12}>
                        <Form.Item
                            name="code"
                            label="Code"
                            rules={[{required: true, message: 'Please enter course code'}]}
                        >
                            <Input placeholder="Please enter course code" maxLength={5}/>
                        </Form.Item>
                    </Col>
                </Row>
                <Row>
                    <Col span={12}>
                        <Form.Item >
                            <Button type="primary" htmlType="submit">
                                Submit
                            </Button>
                        </Form.Item>
                    </Col>
                </Row>
                <Row>
                    {submitting && <Spin indicator={antIcon} />}
                </Row>
            </Form>
        </Drawer>
    };

    return Demo();
}

export default CourseDrawerForm;