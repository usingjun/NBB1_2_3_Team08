import {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import axios from 'axios';
import axiosInstance from '../../pages/axiosInstance';

const AttendanceCheck = () => {
    const memberId = localStorage.getItem('memberId');
    const [attendanceChecked, setAttendanceChecked] = useState(false); // 출석 체크 여부
    const navigate = useNavigate();

    const checkAndMarkAttendance = async () => {
        try {
            const response = await axiosInstance.get(`attendances/${memberId}/today`);
            const isAttendedToday = response.data.attendance;

            if (!isAttendedToday) {
                const token = localStorage.getItem('accessToken');
                await axiosInstance.post('attendances', {memberId: Number(memberId)}, {headers: {'Authorization' : `Bearer ${token}`}});
            }
            setAttendanceChecked(true); // 출석 체크 완료
        } catch (error) {
            console.error("출석 체크 중 오류가 발생했습니다.", error);
        }
    };

    useEffect(() => {
        if (!memberId) {
            return;
        }

        // 첫 출석 체크 시도
        checkAndMarkAttendance();

        const now = new Date();
        const millisUntilMidnight =
            new Date(now.getFullYear(), now.getMonth(), now.getDate() + 1, 0, 0, 0) - now;

        // 자정이 될 때 출석 체크
        const midnightTimeout = setTimeout(() => {
            checkAndMarkAttendance(); // 자정에 출석 체크
            setInterval(checkAndMarkAttendance, 24 * 60 * 60 * 1000); // 이후 24시간마다 출석 체크
        }, millisUntilMidnight);

        return () => clearTimeout(midnightTimeout);
    }, [memberId, attendanceChecked]);

    return null;
};

export default AttendanceCheck;
