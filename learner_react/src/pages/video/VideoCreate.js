import React, { useState } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom"; // useParams 추가
import styled from "styled-components";
import { jwtDecode } from "jwt-decode";


const Video_Url = "http://localhost:8080/video";

const AddVideo = ( ) => {
    const { courseId } = useParams();
    const [title, setTitle] = useState("");
    const [url, setUrl] = useState("");
    const [description, setDescription] = useState("");
    const navigate = useNavigate();
    const [error, setError] = useState(null);


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
            const role = decodedToken.role;

            const payload = {
                course_Id: courseId,
                title,
                url,
                description,
            };

            await axios.post('http://localhost:8080/video', payload, { withCredentials: true });

            // 성공 메시지와 페이지 리디렉션
            alert("강의 생성에 성공하였습니다."); // alert 추가
            navigate("/courses/list"); // 상대 경로로 수정
        } catch (err) {
            setError("강좌 생성에 실패했습니다.");
        }
    };


    return (
        <FormContainer>
            <h2>비디오 추가</h2>
            <form onSubmit={handleSubmit}>
                <Label>
                    제목:
                    <Input type="text" value={title} onChange={(e) => setTitle(e.target.value)} required />
                </Label>
                <Label>
                    URL:
                    <Input type="text" value={url} onChange={(e) => setUrl(e.target.value)} required />
                </Label>
                <Label>
                    설명:
                    <Input type="text" value={description} onChange={(e) => setDescription(e.target.value)} />
                </Label>
                <Button type="submit">추가</Button>
            </form>
        </FormContainer>
    );
};

// 스타일 컴포넌트들
const FormContainer = styled.div`
    max-width: 400px;
    margin: 0 auto;
    padding: 2rem;
    background: #f9f9f9;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
`;

const Label = styled.label`
    display: block;
    margin-bottom: 0.5rem;
`;

const Input = styled.input`
    width: 100%;
    padding: 0.5rem;
    margin-bottom: 1rem;
    border: 1px solid #ddd;
    border-radius: 4px;
`;

const Button = styled.button`
    padding: 0.5rem 1rem;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;

    &:hover {
        background-color: #0056b3;
    }
`;

export default AddVideo;
