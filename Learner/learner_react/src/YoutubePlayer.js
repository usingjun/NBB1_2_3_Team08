import { useEffect, useRef, useState } from 'react';
import { useLocation, useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import styled from 'styled-components';

const YoutubePlayer = () => {
    const { videoId } = useParams();
    const location = useLocation();
    const navigate = useNavigate();
    const playerRef = useRef(null);
    const [totalDuration, setTotalDuration] = useState(0);
    const [currentTime, setCurrentTime] = useState(0);
    const [courseVideos, setCourseVideos] = useState([]);
    const [currentVideoIndex, setCurrentVideoIndex] = useState(-1);
    const [isLoading, setIsLoading] = useState(true);
    const [memberId] = useState(localStorage.getItem('memberId'));
    const hasPosted = useRef(false);

    const videoEntityId = location.state?.videoEntityId;
    const youtubeId = location.state?.youtubeId;
    const courseId = location.state?.courseId;

    const embedUrl = `https://www.youtube.com/embed/${youtubeId}?enablejsapi=1`;

    useEffect(() => {
        const createStudyTable = async () => {
            if (hasPosted.current) return;
            hasPosted.current = true;
            try {
                await axios.post('http://localhost:8080/study-tables', {
                    memberId: memberId,
                    studyTime: 0,
                    completed: 0,
                }, { withCredentials: true });
            } catch (error) {
                console.error('데이터 생성 중 에러:', error);
            }
        };

        createStudyTable();
    }, [memberId]);

    const updateStudyTable = async (studyTime, completed) => {
        try {
            await axios.put('http://localhost:8080/study-tables/today', {
                memberId: memberId,
                studyTime: studyTime,
                completed: completed,
            }, { withCredentials: true });
        } catch (error) {
            console.error('데이터 업데이트 중 에러:', error);
        }
    };

    useEffect(() => {
        const timer = setInterval(() => {
            updateStudyTable(1, 0); // 1분마다 1분 추가
        }, 60000);

        return () => clearInterval(timer);
    }, [memberId]);

    useEffect(() => {
        const fetchCourseVideos = async () => {
            if (!courseId) {
                console.error("courseId is missing!");
                return;
            }

            setIsLoading(true);
            try {
                const response = await axios.get(`http://localhost:8080/course/video/${courseId}`, { withCredentials: true });
                const videos = response.data;
                setCourseVideos(videos);
                const index = videos.findIndex(v => v.video_Id === parseInt(videoId));
                setCurrentVideoIndex(index);
            } catch (error) {
                console.error("강의 목록 가져오기 실패:", error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchCourseVideos();
    }, [courseId, videoId]);

    useEffect(() => {
        const fetchVideoDuration = async () => {
            const apiKey = "AIzaSyDoEwQOJ6Igsm9dCnk1b1y1sqzG3qdoEw0";
            const url = `https://www.googleapis.com/youtube/v3/videos?id=${youtubeId}&key=${apiKey}&part=contentDetails`;

            try {
                const response = await fetch(url);
                if (!response.ok) throw new Error(`비디오 길이 가져오기 오류: ${response.status} ${response.statusText}`);

                const data = await response.json();
                if (data.items && data.items.length > 0) {
                    const duration = data.items[0].contentDetails.duration;
                    const seconds = convertISO8601ToSeconds(duration);
                    setTotalDuration(seconds);
                    sendTotalDuration(seconds);
                } else {
                    console.error("비디오를 찾을 수 없습니다.");
                }
            } catch (error) {
                console.error("비디오 길이 가져오기 중 에러:", error);
            }
        };

        if (videoId) {
            fetchVideoDuration();
        }
    }, [videoId, youtubeId]);

    const convertISO8601ToSeconds = (duration) => {
        const regex = /PT(\d+H)?(\d+M)?(\d+S)?/;
        const matches = regex.exec(duration);

        const hours = parseInt(matches[1]) || 0;
        const minutes = parseInt(matches[2]) || 0;
        const seconds = parseInt(matches[3]) || 0;

        return hours * 3600 + minutes * 60 + seconds;
    };

    useEffect(() => {
        const loadYouTubePlayer = () => {
            window.YT.ready(() => {
                new window.YT.Player(playerRef.current, {
                    videoId: youtubeId,
                    events: {
                        onReady: onPlayerReady,
                        onStateChange: onPlayerStateChange,
                    },
                });
            });
        };

        if (!window.YT) {
            const tag = document.createElement('script');
            tag.src = "https://www.youtube.com/iframe_api";
            const firstScriptTag = document.getElementsByTagName('script')[0];
            firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
            tag.onload = loadYouTubePlayer;
        } else {
            loadYouTubePlayer();
        }
    }, [youtubeId]);

    const onPlayerReady = (event) => {
        console.log('플레이어가 준비되었습니다.');
    };

    const onPlayerStateChange = async (event) => {
        if (event.data === window.YT.PlayerState.ENDED) {
            console.log('동영상이 종료되었습니다!');
            await updateStudyTable(0, 1); // 학습 완료 여부를 서버에 업데이트
        }
    };

    const sendTotalDuration = async (duration) => {
        try {
            const response = await fetch('http://localhost:8080/video/savePlayTime', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    videoId: videoEntityId,
                    totalDuration: duration
                }),
            });
            const data = await response.json();
            console.log('전체 동영상 시간 전송 성공:', data);
        } catch (error) {
            console.error('오류:', error);
        }
    };

    const handlePrevVideo = async () => {
        if (isLoading || currentVideoIndex <= 0) return;

        const prevVideo = courseVideos[currentVideoIndex - 1];
        navigate(`/video/${prevVideo.video_Id}/play`, {
            state: {
                videoEntityId: prevVideo.video_Id,
                youtubeId: extractVideoId(prevVideo.url),
                courseId: courseId
            }
        });
    };

    const handleNextVideo = async () => {
        if (isLoading || currentVideoIndex >= courseVideos.length - 1) return;

        const nextVideo = courseVideos[currentVideoIndex + 1];
        navigate(`/video/${nextVideo.video_Id}/play`, {
            state: {
                videoEntityId: nextVideo.video_Id,
                youtubeId: extractVideoId(nextVideo.url),
                courseId: courseId
            }
        });
    };

    const extractVideoId = (url) => {
        const regex = /[?&]v=([^&#]*)/;
        const match = url.match(regex);
        return match ? match[1] : null;
    };

    if (!courseId) {
        return <div>잘못된 접근입니다. courseId가 필요합니다.</div>;
    }

    return (
        <PlayerContainer>
            <iframe
                id="youtube-player"
                width="560"
                height="315"
                src={embedUrl}
                title="YouTube Video"
                frameBorder="0"
                allowFullScreen
                ref={playerRef} // ref를 iframe에 추가
            ></iframe>
            <InfoContainer>
                <NavigationContainer>
                    <NavButton onClick={handlePrevVideo} disabled={isLoading || currentVideoIndex <= 0}>
                        {isLoading ? "로딩 중..." : "이전 강의"}
                    </NavButton>

                    <VideoProgress>
                        {isLoading ? "로딩 중..." : `${currentVideoIndex + 1} / ${courseVideos.length}`}
                    </VideoProgress>

                    <NavButton onClick={handleNextVideo} disabled={isLoading || currentVideoIndex >= courseVideos.length - 1}>
                        {isLoading ? "로딩 중..." : "다음 강의"}
                    </NavButton>
                </NavigationContainer>
            </InfoContainer>
        </PlayerContainer>
    );
};

const PlayerContainer = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
`;

const InfoContainer = styled.div`
    margin-top: 10px;
`;

const NavigationContainer = styled.div`
    display: flex;
    justify-content: space-between;
    align-items: center;
`;

const NavButton = styled.button`
    padding: 10px;
    background-color: #3cb371;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;

    &:disabled {
        background-color: grey;
        cursor: not-allowed;
    }
`;

const VideoProgress = styled.div`
    font-weight: bold;
`;

export default YoutubePlayer;
