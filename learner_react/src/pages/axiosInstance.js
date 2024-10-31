import Cookies from 'js-cookie';
import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080', // API URL 설정
    withCredentials: true, // 쿠키를 포함하도록 설정
});

// 응답 인터셉터
axiosInstance.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (error.response && error.response.status === 401) {
            // JWT가 만료되었음을 감지했을 때
            // 로그아웃 처리
            Cookies.remove('Authorization'); // 쿠키에서 토큰 제거

            // 메인 페이지로 이동 후 새로 고침
            window.location.href = 'http://localhost:3000/course?sessionExpired=true'; // 메인 페이지로 이동
        }

        return Promise.reject(error);
    }
);

export default axiosInstance;
