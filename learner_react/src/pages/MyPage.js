import React, { useEffect, useState } from "react";
import styled from "styled-components";
import WeeklyStudyTable from "../components/study-table/WeeklyStudyTable";
import YearlyStudyTable from "../components/study-table/YearlyStudyTable";
import axiosInstance from './axiosInstance'; // axiosInstance import
import Cookies from 'js-cookie'; // js-cookie 패키지 import

const MyPage = () => {
    const [userInfo, setUserInfo] = useState(null);
    const [selectedFile, setSelectedFile] = useState(null);
    const [errorMessage, setErrorMessage] = useState("");
    const [isHover, setIsHover] = useState(false);

    useEffect(() => {
        const memberId = localStorage.getItem("memberId");
        fetchUserInfo(memberId);
    }, []);

    const fetchUserInfo = async (memberId) => {
        try {
            const response = await axiosInstance.get(`/members/${memberId}`, {
                // credentials: "include"가 필요 없으므로 제거
            });

            if (response.status === 200) {
                const data = await response.data; // 응답 데이터는 이미 axiosInstance에서 처리됨
                setUserInfo(data);
            } else {
                handleFetchError("사용자 정보 로드 실패", response.status);
            }
        } catch (error) {
            handleFetchError("API 호출 중 오류 발생", error);
        }
    };

    const handleFetchError = (message, error) => {
        console.error(message, error);
        setErrorMessage(message);
    };

    const handleLogout = () => {
        Cookies.remove('Authorization'); // Authorization 쿠키 제거
        localStorage.removeItem("memberId");
        window.location.href = "/"; // 메인 페이지로 이동
    };

    const handleFileChange = async (event) => {
        const file = event.target.files[0];
        if (file) {
            setSelectedFile(file);
            await uploadProfileImage(file);
        }
    };

    const uploadProfileImage = async (file) => {
        const memberId = localStorage.getItem("memberId");
        const formData = new FormData();
        formData.append("file", file);

        try {
            const response = await axiosInstance.put(`/members/${memberId}/image`, formData);
            if (response.ok) {
                setUserInfo((prev) => ({ ...prev, profileImage: response.data.profileImage })); // 수정
                alert(response.data.message || "이미지 업로드 성공!");
                window.location.reload();
            } else {
                handleFetchError("이미지 업로드 실패", response.status);
            }
        } catch (error) {
            handleFetchError("이미지 업로드 중 오류 발생", error);
        }
    };

    const handleUploadClick = () => {
        document.getElementById("fileInput").click();
    };

    const handleDeleteImage = async () => {
        const memberId = localStorage.getItem("memberId");
        try {
            const response = await axiosInstance.delete(`/members/${memberId}/image`);

            if (response.ok) {
                setUserInfo((prev) => ({ ...prev, profileImage: null }));
                alert("이미지가 성공적으로 삭제되었습니다.");
                window.location.reload();
            } else {
                handleFetchError("이미지 삭제 실패", response.status);
            }
        } catch (error) {
            handleFetchError("이미지 삭제 중 오류 발생", error);
        }
    };

    const handleWithdraw = async () => {
        const memberId = localStorage.getItem("memberId");
        try {
            const response = await axiosInstance.delete(`/members/${memberId}`);

            if (response.ok) {
                alert("회원탈퇴가 완료되었습니다.");
                handleLogout();
            } else {
                handleFetchError("회원탈퇴 실패", response.status);
            }
        } catch (error) {
            handleFetchError("회원탈퇴 중 오류 발생", error);
        }
    };

    if (!userInfo) {
        return <LoadingMessage>로딩 중...</LoadingMessage>;
    }

    const profileImageSrc = userInfo.profileImage
        ? `data:image/jpeg;base64,${userInfo.profileImage}`
        : "http://localhost:8080/images/default_profile.jpg";

    return (
        <Container>
            <Title>마이페이지</Title>
            <ProfileSection
                onMouseEnter={() => setIsHover(true)}
                onMouseLeave={() => setIsHover(false)}
            >
                <ProfilePicture src={profileImageSrc} alt="Profile" />
                {isHover && (
                    <UploadButton onClick={handleUploadClick}>+</UploadButton>
                )}
                {userInfo.profileImage && (
                    <DeleteButton onClick={handleDeleteImage}>이미지 삭제</DeleteButton>
                )}
                <input
                    type="file"
                    id="fileInput"
                    style={{ display: "none" }}
                    accept="image/*"
                    onChange={handleFileChange}
                />
            </ProfileSection>
            <UserInfoSection>
                <UserInfo>
                    <InfoItem><strong>닉네임:</strong> {userInfo.nickname}</InfoItem>
                    <InfoItem><strong>이메일:</strong> {userInfo.email}</InfoItem>
                    <InfoItem><strong>전화번호:</strong> {userInfo.phoneNumber}</InfoItem>
                </UserInfo>
                <AboutSection>
                    <AboutTitle>자기소개</AboutTitle>
                    <AboutContent>{userInfo.introduction || "자기소개가 없습니다."}</AboutContent>
                </AboutSection>
                <TableContainer>
                    <WeeklyStudyTable />
                    <YearlyStudyTable />
                </TableContainer>
            </UserInfoSection>
            {errorMessage && <ErrorMessage>{errorMessage}</ErrorMessage>}
            <ButtonContainer>
                <StyledButton onClick={() => window.location.href = "/edit-profile"}>회원정보 수정</StyledButton>
                <StyledButton onClick={handleLogout}>로그아웃</StyledButton>
                <StyledButton onClick={handleWithdraw}>회원탈퇴하기</StyledButton>
            </ButtonContainer>
        </Container>
    );
};

export default MyPage;

// 스타일 컴포넌트들 (생략)

const Container = styled.div`
    padding: 4rem;
    max-width: 800px;
    margin: auto;
    text-align: center;
`;

const Title = styled.h1`
    font-size: 2.5rem;
    margin-bottom: 3rem;
`;

const ProfileSection = styled.div`
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 3rem;
    position: relative;
`;

const UploadButton = styled.button`
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: #3cb371;
    color: white;
    border: none;
    border-radius: 50%;
    width: 40px;
    height: 40px;
    cursor: pointer;
    font-size: 1.5rem;
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1;
    &:hover {
        background-color: #218838;
    }
`;

const DeleteButton = styled.button`
    position: absolute;
    top: 10%;
    right: 10%;
    background-color: #dc3545;
    color: white;
    border: none;
    border-radius: 5px;
    padding: 0.5rem 1rem;
    cursor: pointer;
    font-size: 1rem;
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1;
    &:hover {
        background-color: #c82333;
    }
`;

const ProfilePicture = styled.img`
    width: 200px;
    height: 200px;
    border-radius: 50%;
    margin-right: 2rem;
    object-fit: cover;
    border: 5px solid #3cb371;
`;

const UserInfoSection = styled.div`
    text-align: left;
`;

const UserInfo = styled.div`
    font-size: 1.2rem;
`;

const InfoItem = styled.p`
    margin: 0.5rem 0;
`;

const AboutSection = styled.div`
    margin: 2rem 0;
    border: 1px solid #ccc;
    border-radius: 5px;
    padding: 1.5rem;
    background-color: #f9f9f9;
`;

const AboutTitle = styled.h2`
    margin: 0;
    font-size: 1.5rem;
`;

const AboutContent = styled.p`
    font-size: 1.1rem;
`;

const TableContainer = styled.div`
    margin-top: 2rem;
`;

const LoadingMessage = styled.p`
    font-size: 1.5rem;
    color: #888;
`;

const ButtonContainer = styled.div`
    display: flex;
    justify-content: space-around;
    margin-top: 2rem;
`;

const StyledButton = styled.button`
    padding: 0.7rem 1.5rem;
    font-size: 1rem;
    border: none;
    border-radius: 5px;
    background-color: #007bff;
    color: white;
    cursor: pointer;
    &:hover {
        background-color: #0056b3;
    }
`;

const ErrorMessage = styled.div`
    color: red;
    font-weight: bold;
    margin-top: 1rem;
`;
