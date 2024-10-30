import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import styled from "styled-components";
import { jwtDecode } from "jwt-decode";

const Course_Url = "http://localhost:8080/course";

const CourseList = () => {
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [role, setRole] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchCourses = async () => {
            setLoading(true);
            try {
                const token = document.cookie
                    .split('; ')
                    .find(row => row.startsWith('Authorization='))
                    ?.split('=')[1];

                if (token) {
                    const decodedToken = jwtDecode(token);
                    const nickname = decodedToken.mid;
                    const userRole = decodedToken.role;
                    setRole(userRole);
                    const response = await axios.get(`${Course_Url}/instruct/list/${nickname}`, { withCredentials: true });
                    setCourses(response.data);
                } else {
                    setError("로그인이 필요합니다.");
                }
            } catch (error) {
                console.error("강좌 목록 가져오는 중 오류 발생:", error);
                setError("강좌 목록을 가져오는 데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };

        fetchCourses();
    }, []);

    const handleUpdateClick = (courseId) => {
        navigate(`/courses/update/${courseId}`);
    };

    const handleDeleteClick = async (courseId) => {
        if (window.confirm("정말로 이 강좌를 삭제하시겠습니까?")) {
            try {
                await axios.delete(`${Course_Url}/${courseId}`, { withCredentials: true });
                setCourses(courses.filter(course => course.courseId !== courseId));
            } catch (error) {
                console.error("강좌 삭제 중 오류 발생:", error);
                setError("강좌를 삭제하는 데 실패했습니다.");
            }
        }
    };

    const handleCourseDetailClick = (courseId) => {
        navigate(`/courses/${courseId}`);
    };

    const handleVideoEditClick = (courseId) => {
        navigate(`/video/Instructor/${courseId}`);
    };

    if (loading) return <LoadingMessage>로딩 중...</LoadingMessage>;
    if (error) return <ErrorMessage>{error}</ErrorMessage>;

    return (
        <CourseListContainer>
            <Header>강좌 목록</Header>
            {role === "INSTRUCTOR" ? (
                <Link to="/courses/create">
                    <StyledButton primary>강좌 생성</StyledButton>
                </Link>
            ) : (
                <p>강좌 생성을 위해 강사로 로그인해주세요.</p>
            )}
            {courses.length > 0 ? (
                courses.map(course => (
                    <CourseItem key={course.courseId}>
                        <CourseDetails>
                            <p>강좌 ID: <strong>{course.courseId}</strong></p>
                            <p>강좌 이름: <strong>{course.courseName}</strong></p>
                            <p>등록 날짜: <strong>{new Date(course.createdAt).toLocaleDateString()}</strong></p>
                        </CourseDetails>
                        <ButtonContainer>
                            <StyledButton onClick={() => handleUpdateClick(course.courseId)} secondary>수정</StyledButton>
                            <StyledButton onClick={() => handleDeleteClick(course.courseId)} delete>삭제</StyledButton>
                            <StyledButton onClick={() => handleCourseDetailClick(course.courseId)}>상세정보</StyledButton>
                            <StyledButton onClick={() => handleVideoEditClick(course.courseId)} secondary>비디오 수정</StyledButton>
                        </ButtonContainer>
                    </CourseItem>
                ))
            ) : (
                <p>강좌가 없습니다.</p>
            )}
        </CourseListContainer>
    );
};

// 스타일 컴포넌트들
const CourseListContainer = styled.div`
    max-width: 800px;
    margin: 0 auto;
    padding: 2rem;
    background: #f9f9f9;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
`;

const Header = styled.h2`
    text-align: center;
    color: #333;
    margin-bottom: 1.5rem;
`;

const LoadingMessage = styled.p`
    text-align: center;
    color: #007bff;
`;

const ErrorMessage = styled.p`
    text-align: center;
    color: red;
    font-weight: bold;
`;

const CourseItem = styled.div`
    padding: 1rem;
    border: 1px solid #ddd;
    border-radius: 8px;
    margin-bottom: 1rem;
    background-color: #fff;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
`;

const CourseDetails = styled.div`
    display: flex;
    justify-content: space-between;
    align-items: center;
`;

const ButtonContainer = styled.div`
    display: flex;
    justify-content: space-between;
    margin-top: 1rem;
`;

const StyledButton = styled.button`
    padding: 0.5rem 1rem;
    background-color: ${props =>
            props.primary ? "#007bff" :
                    props.secondary ? "#6c757d" :
                            props.delete ? "#dc3545" : "#28a745"
    };
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s;

    &:hover {
        background-color: ${props =>
                props.primary ? "#0056b3" :
                        props.secondary ? "#5a6268" :
                                props.delete ? "#c82333" : "#218838"
        };
    }
`;

export default CourseList;
