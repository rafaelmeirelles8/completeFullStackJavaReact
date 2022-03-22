import {Drawer, Input, Col, Select, Form, Row, Button, Spin} from 'antd';
import {createStudent, getStudentById, updateStudent} from "../client";
import {LoadingOutlined} from "@ant-design/icons";
import {useEffect, useState} from "react";
import {errorNotification, successNotification} from "../Notification";

const {Option} = Select;
const antIcon = <LoadingOutlined style={{ fontSize: 24 }} spin />;

function StudentDrawerForm({showDrawer, setShowDrawer, fetchStudents, studentToEditID}) {
    const onCLose = () => {
        setShowDrawer(false);
        form.resetFields();
    }
    const [submitting, setSubmitting] = useState(false);
    const [form] = Form.useForm();

    useEffect(() => {
        if(showDrawer && studentToEditID > 0) {
            getStudentById(studentToEditID)
                .then((res) => res.json())
                .then(data => {
                    form.setFieldsValue({ name: data.name });
                    form.setFieldsValue({ email: data.email });
                    form.setFieldsValue({ gender: data.gender });
                })
                .catch(() => {
                    //err.response.json().then(res => {
                    //    errorNotification('Student not found', `${res.message} [${res.status}]`);
                    //})
                });
        }
    });

    const onFinish = student => {
        setSubmitting(true);

        if(studentToEditID <= 0) {
            createStudent(student)
                .then(() => {
                    successNotification('Student created successfully',
                        `${student.name} was added to the system`);
                    onCLose();
                    fetchStudents();
                })
                .catch(err => {
                    err.response.json().then(res => {
                        errorNotification('Student was not created', `${res.message} [${res.status}]`);
                    })

                })
                .finally(() => setSubmitting(false));
        }
        else {
            student.id = studentToEditID;
            updateStudent(student)
                .then(() => {
                    successNotification('Student updated successfully',
                        `${student.name} was updated to the system`);
                    onCLose();
                    fetchStudents();
                })
                .catch(err => {
                    err.response.json().then(res => {
                        errorNotification('Student was not updated', `${res.message} [${res.status}]`);
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
            title="Create new student"
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
                            rules={[{required: true, message: 'Please enter student name'}]}
                        >
                            <Input placeholder="Please enter student name"/>
                        </Form.Item>
                    </Col>
                    <Col span={12}>
                        <Form.Item
                            name="email"
                            label="Email"
                            rules={[{required: true, message: 'Please enter student email'}]}
                        >
                            <Input placeholder="Please enter student email"/>
                        </Form.Item>
                    </Col>
                </Row>
                <Row gutter={16}>
                    <Col span={12}>
                        <Form.Item
                            name="gender"
                            label="Gender"
                            rules={[{required: true, message: 'Please select a gender'}]}
                        >
                            <Select placeholder="Please select a gender">
                                <Option value="MALE">Male</Option>
                                <Option value="FEMALE">Female</Option>
                                <Option value="OTHER">Other</Option>
                            </Select>
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

export default StudentDrawerForm;