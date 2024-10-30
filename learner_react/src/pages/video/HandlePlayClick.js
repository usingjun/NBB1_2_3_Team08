import axios from "axios";
import Cookies from "js-cookie"; // 쿠키 관리 라이브러리 추가

const extractVideoId = (url) => {
    const regex = /[?&]v=([^&#]*)/;
    const match = url.match(regex);
    return match ? match[1] : null;
};

// 비디오 재생 로직을 별도의 함수로 분리
const navigateToVideoPlayer = (navigate, video, courseId) => {
    const youtubeId = extractVideoId(video.url);
    navigate(`/video/${video.video_Id}/play`, {
        state: {
            videoEntityId: video.video_Id,
            youtubeId: youtubeId,
            courseId: courseId // courseId를 state에 추가
        }
    });
};

export const handlePlayClick = async (courseId, video, navigate, setError, role, memberNickname) => {
    try {
        const memberId = localStorage.getItem('memberId');

        // USER 역할일 경우 구매 여부 확인
        if (role === "USER") {
            const response = await axios.get(`http://localhost:8080/course/${courseId}/purchase?memberId=${memberId}`, { withCredentials: true });
            const purchased = response.data; // boolean 값으로 받아옴

            // 구매 여부가 boolean인지 확인
            if (typeof purchased === 'boolean') {
                if (purchased) {
                    navigateToVideoPlayer(navigate, video, courseId); // 구매 후 비디오 재생 페이지로 이동
                } else {
                    alert("비디오 구매가 필요합니다.");
                }
            } else {
                alert("비디오 구매 여부 확인에 실패했습니다.");
            }
        }
        // INSTRUCTOR 역할일 경우
        else if (role === "INSTRUCTOR") {
            if (!video.course_Id) {
                console.error("비디오 객체에서 course_Id를 찾을 수 없습니다:", video);
                alert("비디오 정보를 확인할 수 없습니다.");
                return;
            }

            // 강의 정보 조회
            const courseResponse = await axios.get(`http://localhost:8080/course/${video.course_Id}`);
            const courseData = courseResponse.data;

            console.log("강의 정보:", courseData); // 강의 데이터 확인용 로그

            if (courseData.memberNickname === memberNickname) {
                navigateToVideoPlayer(navigate, video, courseId); // 본인의 비디오일 경우 비디오 재생 페이지로 이동
            } else {
                alert("본인의 비디오가 아닙니다."); // 본인의 비디오가 아닐 경우 알림
            }
        }
        // ADMIN 역할일 경우
        else if (role === "ADMIN") {
            navigateToVideoPlayer(navigate, video, courseId); // ADMIN일 경우 비디오 재생 페이지로 이동
        }

        else {
            alert("로그인 후 이용해주세요");
        }
    } catch (error) {
        console.error("구매 확인 중 오류 발생:", error);
        setError("구매 확인에 실패했습니다.");
    }
};
