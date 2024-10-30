import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const MyCourses = () => {
    const [courses, setCourses] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const memberId = localStorage.getItem('memberId');

        if (memberId) {
            fetch(`http://localhost:8080/course/${memberId}/list`, { credentials: 'include' })
                .then(response => response.json())
                .then(data => setCourses(data))
                .catch(error => console.error('Error fetching courses:', error));
        } else {
            console.error('memberId not found in localStorage');
        }
    }, []);

    const handleCourseClick = (courseId) => {
        navigate(`/courses/${courseId}`);
    };

    return (
        <div style={styles.container}>
            <h1 style={styles.header}>내 수강 정보</h1>
            {courses.length > 0 ? (
                <ul style={styles.courseList}>
                    {courses.map((course, index) => (
                        <li key={index} style={styles.courseItem}>
                            <div onClick={() => handleCourseClick(course.courseId)} style={styles.courseLink}>
                                <div>
                                    <span style={styles.courseName}>{course.courseName}</span>
                                    <p style={styles.instructor}>강사: <span style={styles.instructorName}>{course.instructor}</span></p>
                                    <p style={styles.purchaseDate}>구매일: <span style={styles.purchaseDateText}>{new Date(course.purchaseDate).toLocaleDateString()}</span></p>
                                </div>
                                <span style={styles.courseArrow}>→</span>
                            </div>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>수강 중인 과정이 없습니다.</p>
            )}
        </div>
    );
};

const styles = {
    container: {
        maxWidth: '800px',
        margin: '40px auto',
        padding: '30px',
        backgroundColor: '#fff',
        borderRadius: '16px',
        boxShadow: '0 4px 16px rgba(0, 0, 0, 0.1)',
    },
    header: {
        fontSize: '32px',
        color: '#333',
        marginBottom: '20px',
        borderBottom: '3px solid #007bff',
        paddingBottom: '10px',
        fontWeight: 'bold',
    },
    courseList: {
        listStyleType: 'none',
        padding: 0,
    },
    courseItem: {
        marginBottom: '20px',
        transition: 'transform 0.3s ease',
    },
    courseLink: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: '15px',
        backgroundColor: '#f9f9f9',
        borderRadius: '10px',
        color: '#333',
        textDecoration: 'none',
        boxShadow: '0 2px 8px rgba(0, 0, 0, 0.05)',
        transition: 'box-shadow 0.3s ease, transform 0.3s ease',
        cursor: 'pointer',
    },
    instructor: {
        margin: '5px 0',
        fontSize: '16px',
        color: '#555',
    },
    instructorName: {
        color: '#007bff', // 강사 이름 색상
    },
    purchaseDate: {
        margin: '5px 0',
        fontSize: '14px',
        color: '#777',
        paddingLeft: '5px', // 여백 추가
    },
    purchaseDateText: {
        fontWeight: 'normal',
    },
    courseName: {
        fontWeight: 'bold',
        fontSize: '18px',
    },
    courseArrow: {
        fontSize: '20px',
        color: '#007bff',
        transition: 'transform 0.3s ease',
    },
    courseItemHover: {
        backgroundColor: '#e0e0e0', // hover 시 배경색
    },
};

export default MyCourses;
