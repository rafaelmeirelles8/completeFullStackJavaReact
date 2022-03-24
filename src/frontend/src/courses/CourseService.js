import fetch from 'unfetch';

const checkStatus = response => {
    if(response.ok) {
        return response;
    }

    const error = new Error(response.statusText);
    error.response = response;
    return Promise.reject(error);
}

export const getAllCourses = () =>
    fetch("api/v1/courses/")
    .then(checkStatus);

export const getCourseById = (courseId) =>
    fetch("api/v1/courses/" + courseId)
        .then(checkStatus);

export const createCourse = course =>
    fetch("api/v1/courses/", {
        headers: {
            'Content-Type': 'application/json'
        },
        method: 'POST',
        body: JSON.stringify(course)
     }
    ).then(checkStatus);

export const updateCourse = course =>
    fetch("api/v1/courses/", {
        headers: {
            'Content-Type': 'application/json'
        },
        method: 'PUT',
        body: JSON.stringify(course)
     }
    ).then(checkStatus);


export const deleteCourse = course =>
    fetch("api/v1/courses/", {
            headers: {
                'Content-Type': 'application/json'
            },
            method: 'DELETE',
            body: JSON.stringify(course)
        }
    ).then(checkStatus);

