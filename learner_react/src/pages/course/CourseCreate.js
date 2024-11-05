import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
import { jwtDecode } from "jwt-decode";
import axiosInstance from "../axiosInstance";

const Course_Url = "http://localhost:8080/course";

const CourseCreate = () => {
    const [courseName, setCourseName] = useState("");
    const [courseDescription, setCourseDescription] = useState("");
    const [coursePrice, setCoursePrice] = useState(0);
    const [courseLevel, setCourseLevel] = useState(1); // 기본값 1
    const [courseAttribute, setCourtAttribute] = useState("");
    const [memberNickname, setMemberNickname] = useState(""); // 직접 입력하는 memberNickname
    const [error, setError] = useState(null);
    const [userRole, setUserRole] = useState(null); // 사용자 역할 저장
    const [userId, setUserId] = useState(null); // 사용자 ID 저장
    const navigate = useNavigate();

    // 컴포넌트가 처음 렌더링될 때 fetchUserRoleAndId 호출
    useEffect(() => {
        const fetchData = async () => {
            try {
                const userData = await fetchUserRoleAndId();
                setUserRole(userData.role);   // 사용자 역할 설정
                setUserId(userData.mid);      // 사용자 ID 설정
                //console.log(userData.role);
                //console.log(userData.mid);
            } catch (error) {
                console.error("Error fetching user role and ID:", error);
            }
        };

        fetchData();
    }, []);


    const fetchUserRoleAndId = async () => {
        //console.log("fetchUserRoleAndId 함수 호출됨!");
        const token = localStorage.getItem('accessToken');

        if (!token) {
            throw new Error("Token not found");
        }

        try {
            const response = await axiosInstance.get('/token/decode', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            const data = response.data;
            return {
                mid: data.mid,
                role: data.role,
                username: data.username
            };

        } catch (error) {
            console.error("Error fetching user data:", error);
            throw error;
        }
    };


    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // 로컬 스토리지에서 accessToken 가져오기
            const token = localStorage.getItem('accessToken');
            if (!token) {
                console.error("로그인된 사용자의 토큰을 찾을 수 없습니다.");
                alert("로그인이 필요합니다. 로그인 후 다시 시도해 주세요.");
                return;
            }

            const payload = {
                courseName,
                courseDescription,
                coursePrice,
                courseLevel,
                courseAttribute,
                memberNickname,
            };

            await axios.post(Course_Url, payload, {
                withCredentials: true,
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('accessToken')}`
                }
            });

            // 성공 메시지와 페이지 리디렉션
            alert("강의 생성에 성공하였습니다."); // alert 추가
            navigate("/courses"); // 상대 경로로 수정
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
                    강사 이름:
                    <Input
                        type="text"
                        value={memberNickname}
                        onChange={(e) => setMemberNickname(e.target.value)}
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
                <Label>
                    강좌 과목:
                    <Input
                        type="text"
                        value={courseAttribute}
                        onChange={(e) => setCourtAttribute((e.target.value))}
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
