import fetch from 'unfetch';

const checkStatus = response => {
    if(response.ok) {
        return response;
    }

    const error = new Error(response.statusText);
    error.response = response;
    return Promise.reject(error);
}


export const getAllStudents = () => 
    fetch("api/v1/students/")
    .then(checkStatus);

export const getStudentById = (studentId) =>
    fetch("api/v1/students/" + studentId)
        .then(checkStatus);

export const createStudent = student =>
    fetch("api/v1/students/", {
        headers: {
            'Content-Type': 'application/json'
        },
        method: 'POST',
        body: JSON.stringify(student)
     }
    ).then(checkStatus);

export const updateStudent = student =>
    fetch("api/v1/students/", {
        headers: {
            'Content-Type': 'application/json'
        },
        method: 'PUT',
        body: JSON.stringify(student)
     }
    ).then(checkStatus);


export const deleteStudent = student =>
    fetch("api/v1/students/", {
            headers: {
                'Content-Type': 'application/json'
            },
            method: 'DELETE',
            body: JSON.stringify(student)
        }
    ).then(checkStatus);

