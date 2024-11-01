import Cookies from 'js-cookie';
import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080', // API URL 설정
});

// 요청 인터셉터
axiosInstance.interceptors.request.use(
    (config) => {
        // 로컬 스토리지에서 accessToken 읽기
        const accessToken = localStorage.getItem('accessToken');
        if (accessToken) {
            // Authorization 헤더에 accessToken 추가
            config.headers['Authorization'] = `Bearer ${accessToken}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 응답 인터셉터
axiosInstance.interceptors.response.use(
    (response) => {
        return response;
    },
    async (error) => {
        if (error.response && error.response.status === 401) {
            // JWT가 만료되었음을 감지했을 때

            // 새로운 access token 요청
            try {
                const response = await axiosInstance.get('/reissue', {
                    withCredentials: true, // 쿠키를 포함하여 요청
                });

                if (response.status === 200) {
                    const { accessToken } = response.data; // 새 accessToken 받기
                    localStorage.setItem('accessToken', accessToken); // 로컬 스토리지에 저장
                    return axiosInstance(error.config); // 원래 요청 다시 시도
                }
            } catch (refreshError) {
                console.error("토큰 갱신 실패:", refreshError);
                window.location.href = 'http://localhost:3000/course?sessionExpired=true'; // 메인 페이지로 이동
            }
        }

        return Promise.reject(error);
    }
);

export default axiosInstance;
