import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
import { jwtDecode } from "jwt-decode";

const Course_Url = "http://localhost:8080/course";

const CourseCreate = () => {
    const [courseName, setCourseName] = useState("");
    const [courseDescription, setCourseDescription] = useState("");
    const [coursePrice, setCoursePrice] = useState(0);
    const [courseLevel, setCourseLevel] = useState(1); // 기본값 1
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // 쿠키에서 Authorization 토큰을 가져오는 로직
            const token = document.cookie
                .split("; ")
                .find(row => row.startsWith("Authorization="))
                ?.split("=")[1];

            if (!token) {
                throw new Error("Authorization 토큰이 없습니다.");
            }

            // JWT 디코딩하여 mid 추출
            const decodedToken = jwtDecode(token);
            const memberNickname = decodedToken.mid;

            const payload = {
                courseName,
                courseDescription,
                coursePrice,
                courseLevel,
                memberNickname,
            };

            await axios.post(Course_Url, payload, { withCredentials: true });

            // 성공 메시지와 페이지 리디렉션
            alert("강의 생성에 성공하였습니다."); // alert 추가
            navigate("/courses/list"); // 상대 경로로 수정
        } catch (err) {
            setError("강좌 생성에 실패했습니다.");
        }
    };

    return (
        <Container>
            <h2>강좌 생성</h2>
            {error && <ErrorMessage>{error}</ErrorMessage>}
            <form onSubmit={handleSubmit}>
                <Label>
                    강좌 이름:
                    <Input
                        type="text"
                        value={courseName}
                        onChange={(e) => setCourseName(e.target.value)}
                        required
                    />
                </Label>

                <Label>
                    강좌 설명:
                    <Input
                        type="text"
                        value={courseDescription}
                        onChange={(e) => setCourseDescription(e.target.value)}
                        required
                    />
                </Label>

                <Label>
                    강좌 가격:
                    <Input
                        type="number"
                        value={coursePrice}
                        onChange={(e) => setCoursePrice(Number(e.target.value))}
                        required
                    />
                </Label>

                <Label>
                    강좌 레벨:
                    <Input
                        type="number"
                        value={courseLevel}
                        onChange={(e) => setCourseLevel(Number(e.target.value))}
                        min="1"
                        max="5"
                        required
                    />
                </Label>

                <Button type="submit">생성</Button>
            </form>
        </Container>
    );
};

export default CourseCreate;

const Container = styled.div`
    max-width: 400px;
    margin: 0 auto;
    padding: 2rem;
    background: #f9f9f9;
    border-radius: 8px;
`;

const Label = styled.label`
    display: block;
    margin-bottom: 1rem;
`;

const Input = styled.input`
    width: 100%;
    padding: 0.5rem;
    margin-top: 0.5rem;
`;

const Button = styled.button`
    padding: 0.5rem 1rem;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
`;

const ErrorMessage = styled.p`
    color: red;
    font-weight: bold;
`;
