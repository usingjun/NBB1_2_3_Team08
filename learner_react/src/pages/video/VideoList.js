import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import styled from "styled-components";
import { jwtDecode } from "jwt-decode"; // named import
import Cookies from "js-cookie"; // 쿠키 관리 라이브러리 추가
import { handlePlayClick } from "./HandlePlayClick"; // HandlePlayClick 함수 가져오기

const Course_Url = "http://localhost:8080/course";

const VideoList = () => {
    const { courseId } = useParams();
    const [videos, setVideos] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const [role, setRole] = useState(null); // 사용자 역할 상태 추가
    const [memberNickname, setMemberNickName] = useState(null); // 사용자 ID 상태 추가

    useEffect(() => {
        // JWT 디코딩 함수
        const decodeJwt = (token) => {
            try {
                return jwtDecode(token); // named import로 변경
            } catch (error) {
                console.error("JWT 디코딩 오류:", error);
                return null;
            }
        };

        // 사용자 역할과 ID 가져오기
        const token = Cookies.get("Authorization"); // 쿠키에서 토큰 가져오기
        if (token) {
            const decodedToken = decodeJwt(token);
            setRole(decodedToken?.role); // 사용자 역할 설정
            setMemberNickName(decodedToken?.mid); // 사용자 ID 설정
        }

        const fetchVideos = async () => {
            setLoading(true);
            try {
                const response = await axios.get(`${Course_Url}/video/${courseId}`,{ withCredentials: true });
                setVideos(response.data);
            } catch (error) {
                console.error("비디오 목록 가져오는 중 오류 발생:", error.response ? error.response.data : error.message);
                setError("비디오 목록을 가져오는 데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };

        fetchVideos();
    }, [courseId]);

    if (loading) return <Message>로딩 중...</Message>;
    if (error) return <Message $error>{error}</Message>;

    return (
        <Container>
            <Header>비디오 목록</Header>
            {videos.length > 0 ? (
                videos.map((video, index) => (
                    <VideoItem
                        key={video.video_Id}
                        onClick={() => handlePlayClick(courseId, video, navigate, setError, role, memberNickname)} // 전체 항목 클릭 시 재생
                    >
                        <VideoInfo>
                            <Title>{index + 1}. {video.description}</Title>
                        </VideoInfo>
                    </VideoItem>
                ))
            ) : (
                <Message>비디오가 없습니다.</Message>
            )}
        </Container>
    );
};

// 스타일 컴포넌트
const Container = styled.div`
    max-width: 700px;
    margin: 0 auto;
    padding: 2rem;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
`;

const Header = styled.h2`
    text-align: center;
    color: #222;
    margin-bottom: 2rem;
    font-size: 1.8rem;
    font-weight: bold;
`;

const Message = styled.p`
    text-align: center;
    color: ${(props) => (props.$error ? "#e74c3c" : "#007bff")};
    font-size: 1.2rem;
`;

const VideoItem = styled.div`
    padding: 1rem;
    border: 1px solid #eee;
    border-radius: 6px;
    margin-bottom: 1rem;
    background-color: #f7f7f7;
    box-shadow: 0 1px 6px rgba(0, 0, 0, 0.05);
    display: flex;
    justify-content: space-between;
    align-items: center;
    cursor: pointer; // 커서를 포인터로 변경하여 클릭 가능함을 나타냄
`;

const VideoInfo = styled.div`
    flex: 1;
`;

const Title = styled.h3`
    font-size: 1.2rem;
    color: #333;
    margin: 0; // 마진 제거
`;

export default VideoList;