import React, { useEffect, useState } from "react";
import axios from "axios";
import styled from "styled-components";
import { useNavigate } from "react-router-dom";

const CourseManagement = () => {
    const [courses, setCourses] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        // 강의 목록 가져오기
        axios.get("http://localhost:8080/course/list")
            .then((response) => {
                const camelCasedData = response.data.map(course => ({
                    ...course,
                    courseCreatedDate: course.course_created_date // 키 변경
                }));
                setCourses(camelCasedData);
            })
            .catch((error) => {
                console.error("Error fetching the courses:", error);
            });
    }, []);

    const handleDelete = async (courseId) => {
        if (window.confirm("정말 이 강의를 삭제하시겠습니까?")) {
            try {
                const response = await axios.delete(`http://localhost:8080/course/${courseId}`, { withCredentials: true });
                if (response.status === 200) {
                    setCourses((prevCourses) => prevCourses.filter(course => course.courseId !== courseId));
                    alert(response.data.success); // "강좌가 삭제되었습니다." 메시지 표시
                }
            } catch (error) {
                console.error("Error deleting the course:", error);
                if (error.response && error.response.status === 404) {
                    alert("강의를 찾을 수 없습니다.");
                } else {
                    alert("강의 삭제에 실패했습니다.");
                }
            }
        }
    };

    const handleEdit = (courseId) => {
        navigate(`/admin/courses/edit/${courseId}`);
    };

    return (
        <CoursePage>
            <h2>강의 관리</h2>
            <CourseList>
                {courses.length > 0 ? (
                    courses.map((course) => (
                        <CourseItem key={course.courseId}>
                            <CourseInfo>
                                <h3>{course.courseName}</h3>
                                <p>강사명: {course.memberNickname}</p>
                                <p>강의 아이디: {course.courseId}</p>
                            </CourseInfo>
                            <ButtonContainer>
                                <EditButton onClick={() => handleEdit(course.courseId)}>수정</EditButton>
                                <DeleteButton onClick={() => handleDelete(course.courseId)}>삭제</DeleteButton>
                            </ButtonContainer>
                        </CourseItem>
                    ))
                ) : (
                    <p>등록된 강의가 없습니다.</p>
                )}
            </CourseList>
        </CoursePage>
    );
};

export default CourseManagement;

// 스타일 코드
const CoursePage = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 50px;
`;

const CourseList = styled.div`
    width: 100%;
    max-width: 600px;
`;

const CourseItem = styled.div`
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 1rem;
    border: 1px solid #ddd;
    margin-bottom: 1rem;
    border-radius: 5px;
`;

const CourseInfo = styled.div`
    display: flex;
    flex-direction: column;

    h3 {
        margin: 0;
        font-size: 1.2rem;
    }

    p {
        margin: 0.2rem 0;
        font-size: 0.9rem;
    }
`;

const ButtonContainer = styled.div`
    display: flex;
    gap: 0.5rem;
`;

const EditButton = styled.button`
    background-color: #4CAF50;
    color: white;
    padding: 0.5rem 1rem;
    border: none;
    border-radius: 5px;
    cursor: pointer;

    &:hover {
        background-color: #45a049;
    }
`;

const DeleteButton = styled.button`
    background-color: #f44336;
    color: white;
    padding: 0.5rem 1rem;
    border: none;
    border-radius: 5px;
    cursor: pointer;

    &:hover {
        background-color: #d32f2f;
    }
`;
