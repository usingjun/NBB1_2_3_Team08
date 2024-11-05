import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import axiosInstance from "../axiosInstance";
import styled from "styled-components";

const AlarmList = () => {
    const [alarms, setAlarms] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const [memberId, setMemberId] = useState(null);

    // 사용자 역할 및 memberId 확인 (서버로 요청)
    const checkUserRole = async () => {
        try {
            const token = localStorage.getItem('accessToken');

            if (token) {
                // Authorization 헤더에 JWT 토큰 추가
                const response = await axiosInstance.get('/token/decode', {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });

                // 서버 응답에서 memberId 설정
                setMemberId(response.data.mid);
            }
        } catch (error) {
            console.error("토큰 확인 중 오류 발생:", error);
            setError("사용자 정보를 가져오는 데 실패했습니다.");
        }
    };

    // 알람 목록 가져오기
    const fetchAlarms = async () => {
        if (!memberId) {
            setError("로그인이 필요합니다.");
            return;
        }
        setLoading(true); // 로딩 시작
        try {
            const response = await axiosInstance.get(`/alarm/member/${memberId}`, { withCredentials: true });
            if (Array.isArray(response.data)) {
                setAlarms(response.data);
            } else {
                console.error('알람 응답이 배열이 아닙니다:', response.data);
                setError("알람 목록을 가져오는 데 실패했습니다.");
                setAlarms([]);
            }
        } catch (error) {
            console.error("알람 가져오는 중 오류 발생:", error);
            setError("알람 목록을 가져오는 데 실패했습니다.");
            setAlarms([]);
        } finally {
            setLoading(false); // 로딩 종료
        }
    };

    useEffect(() => {
        checkUserRole(); // 사용자 역할 및 memberId 확인
    }, []); // 컴포넌트가 마운트될 때 한 번만 호출

    useEffect(() => {
        if (memberId) {
            fetchAlarms(); // memberId가 설정되면 알람 목록 가져오기
        }
    }, [memberId]); // memberId가 변경될 때마다 호출

    const handleDeleteClick = (alarmId) => {
        if (window.confirm("정말로 이 알람을 삭제하시겠습니까?")) {
            // 삭제 요청을 보낼 수 있는 로직을 추가하세요.
            console.log(`알람 삭제: ${alarmId}`);
        }
    };

    if (loading) return <LoadingMessage>로딩 중...</LoadingMessage>;
    if (error) return <ErrorMessage>{error}</ErrorMessage>;

    return (
        <AlarmListContainer>
            <Header>알람 목록</Header>
            <Link to="/alarms/create">
                <StyledButton primary>알람 생성</StyledButton>
            </Link>
            {alarms.length > 0 ? (
                alarms.map(alarm => (
                    <AlarmItem key={alarm.alarmId}>
                        <h3>{alarm.alarmTitle}</h3>
                        <p>{alarm.alarmContent}</p>
                        <p>생성일: {new Date(alarm.createdAt).toLocaleDateString()}</p>
                        <p>상태: {alarm.alarmStatus ? '활성' : '비활성'}</p>
                        <ButtonContainer>
                            <StyledButton onClick={() => handleDeleteClick(alarm.alarmId)}>삭제</StyledButton>
                        </ButtonContainer>
                    </AlarmItem>
                ))
            ) : (
                <p>알람이 없습니다.</p>
            )}
        </AlarmListContainer>
    );
};

// 스타일 컴포넌트들
const AlarmListContainer = styled.div`
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

const AlarmItem = styled.div`
    padding: 1rem;
    border: 1px solid #ddd;
    border-radius: 8px;
    margin-bottom: 1rem;
    background-color: #fff;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
`;

const ButtonContainer = styled.div`
    display: flex;
    justify-content: flex-end;
    margin-top: 1rem;
`;

const StyledButton = styled.button`
    padding: 0.5rem 1rem;
    background-color: ${props => props.primary ? "#007bff" : "#6c757d"};
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s;

    &:hover {
        background-color: ${props => props.primary ? "#0056b3" : "#5a6268"};
    }
`;

export default AlarmList;
