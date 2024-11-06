import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axiosInstance from "../axiosInstance"; // axiosInstance로 변경
import styled from "styled-components";
import { jwtDecode } from "jwt-decode"; // named import
import Cookies from "js-cookie"; // 쿠키 관리 라이브러리 추가
import { handlePlayClick } from "./HandlePlayClick";

const Course_Url = "http://localhost:8080/course";

const VideoList = () => {
    const { courseId } = useParams();
    const [videos, setVideos] = useState([]);
    const [averages, setAverages] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const [role, setRole] = useState(); // 사용자 역할 상태 추가
    const [memberId, setMemberId] = useState();
    const [memberNickname, setMemberNickName] = useState(); // 사용자 ID 상태 추가

    const getInfoFromToken = async () => {
        const accessToken = localStorage.getItem("accessToken");
        if (accessToken) {
            try {
                const response = await axiosInstance.get('/token/decode');
                setMemberId(response.data.mid);
                setRole(response.data.role);
                setMemberNickName(response.data.username);
            } catch (error) {
                console.error('Failed to get role:', error);
            }
        }
    };

    useEffect(() => {
        // 사용자 정보 먼저 가져오기
        getInfoFromToken();

        const fetchVideos = async () => {
            setLoading(true);
            try {
                const response = await axiosInstance.get(`/course/video/${courseId}`);
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

    useEffect(() => {
        if (videos.length > 0) {
            const fetchAverages = async () => {
                const responses = await Promise.all(
                    videos.map(async (video) => {
                        const response = await axiosInstance.get(`/member-video/${video.videoId}/average`);
                        return response.data;
                    })
                );
                setAverages(responses);
            };
            fetchAverages();
        }
    }, [videos]);

    if (loading) return <Message>로딩 중...</Message>;
    if (error) return <Message $error>{error}</Message>;

    return (
        <Container>
            <Header>비디오 목록</Header>
            {videos.length > 0 ? (
                videos.map((video, index) => (
                    <VideoItem
                        key={video.videoId}
                        onClick={() => handlePlayClick(courseId, video, navigate, setError, role, memberNickname)}
                    >
                        <VideoInfo>
                            <Title>{index + 1}. {video.description}</Title>
                            <Title>평균 학습 시간: {averages[index]}분</Title>
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
    cursor: pointer;
`;

const VideoInfo = styled.div`
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
`;

const Title = styled.h3`
    font-size: 1.2rem;
    color: #333;
    margin: 0;
`;

export default VideoList;
