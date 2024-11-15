import React, {useCallback, useEffect, useState} from 'react';
import axiosInstance from "../../pages/axiosInstance";

const YearlyStudyTable = () => {
    const [year, setYear] = useState(new Date().getFullYear());
    const [yearlySummary, setYearlySummary] = useState([]);
    const [totalCompleted, setTotalCompleted] = useState(0);
    const [totalStudyTime, setTotalStudyTime] = useState(0);
    const [totalAttendanceDays, setTotalAttendanceDays] = useState(0);
    const [attendanceData, setAttendanceData] = useState([]);
    const [memberId, setMemberId] = useState(null);

    const getInfoFromToken = async () => {
        const accessToken = localStorage.getItem("accessToken");
        if (accessToken) {
            try {
                const response = await axiosInstance.get('/token/decode');
                setMemberId(response.data.mid)
            } catch (error) {
                console.error('Failed to get role:', error);
            }
        }
    };

    useEffect(() => {
        getInfoFromToken();
    }, []);

    const fetchData = useCallback(async (year) => {
        try {
            const response = await axiosInstance.get(`/study-tables/${memberId}/yearly-summary?year=${year}`);
            const {yearlyCompleted, yearlyStudyTime, yearlySummary} = response.data;

            setTotalCompleted(yearlyCompleted);
            setTotalStudyTime(yearlyStudyTime);

            const modifiedSummary = [];
            yearlySummary.forEach(([month, week, completed]) => {
                modifiedSummary.push({month, week, completed});
            });

            setYearlySummary(modifiedSummary);
        } catch (error) {
            console.error("데이터를 가져오는 중 오류 발생:", error);
        }
    }, [memberId]);

    const getYearlyAttendance = useCallback(async (year) => {
        try {
            const response = await axiosInstance.get(`/attendances/${memberId}/yearly-summary`, {
                params: {year},
            });
            const {yearlySummary, attendanceDays} = response.data;
            setAttendanceData(yearlySummary);
            setTotalAttendanceDays(attendanceDays);
        } catch (error) {
            console.error("출석 데이터를 가져오는 중 오류 발생:", error);
        }
    }, [memberId]);

    useEffect(() => {
        if (memberId) {
            fetchData(year);
            getYearlyAttendance(year);
        }
    }, [year, memberId, fetchData, getYearlyAttendance]);

    const handlePreviousYear = () => {
        setYear(year - 1);
    };

    const handleNextYear = () => {
        setYear(year + 1);
    };

    const getColorByCompleted = (completed) => {
        if (completed === 0) return '#f0f0f0';
        else if (completed > 0 && completed <= 5) return '#c6e48b';
        else if (completed > 5 && completed <= 10) return '#b2e0b2';
        else if (completed > 10 && completed <= 20) return '#7ee6b7';
        else if (completed > 20) return '#239a3b';
        else return 'black';
    };

    return (
        <div className="yearly-study-table">
            <h2 style={{display: 'inline-block', marginRight: '20px'}}>연간 통계</h2>
            <div className="year-navigation" style={{display: 'flex', alignItems: 'center', float: 'right'}}>
                <div className="arrow left-arrow" onClick={handlePreviousYear}>{"<"}</div>
                <span style={{fontSize: '24px', fontWeight: 'bold', margin: '0 10px'}}>{year}</span>
                <div className="arrow right-arrow" onClick={handleNextYear}>{">"}</div>
            </div>
            <table>
                <thead>
                <tr>
                    <th></th>
                    {[...Array(12)].map((_, index) => (
                        <th key={index}>{index + 1}월</th>
                    ))}
                </tr>
                </thead>
                <tbody>
                {[...Array(5)].map((_, weekIndex) => (
                    <tr key={weekIndex}>
                        <td>{weekIndex + 1}주차</td>
                        {[...Array(12)].map((_, monthIndex) => {
                            const completedEntry = yearlySummary.find(({month, week}) =>
                                month === monthIndex + 1 && week === weekIndex + 1
                            ) || {completed: 0};
                            const completed = completedEntry.completed;
                            const color = getColorByCompleted(completed);

                            const isAttendanceMarked = attendanceData.some(([month, week]) =>
                                month === monthIndex + 1 && week === weekIndex + 1
                            );

                            return (
                                <td key={monthIndex} className="study-cell">
                                    <div style={{
                                        backgroundColor: color,
                                        width: '40px',
                                        height: '40px',
                                        borderRadius: '4px',
                                        display: 'flex',
                                        justifyContent: 'center',
                                        alignItems: 'center',
                                        margin: 'auto',
                                        boxShadow: isAttendanceMarked ? '0 0 1px 2px coral' : '0 0 5px rgba(0, 0, 0, 0.2)',
                                        transition: 'transform 0.2s',
                                    }}></div>
                                </td>
                            );
                        })}
                    </tr>
                ))}
                </tbody>
            </table>
            <div className="completion-summary"
                 style={{marginTop: '20px', display: 'flex', justifyContent: 'space-between'}}>
                <h3>완료한 수업: {totalCompleted} 개</h3>
                <div style={{display: 'flex', alignItems: 'center'}}>
                    {[
                        {label: "0개", color: '#f0f0f0'},
                        {label: "1~5개", color: '#c6e48b'},
                        {label: "6~10개", color: '#b2e0b2'},
                        {label: "11~20개", color: '#7ee6b7'},
                        {label: "21개 이상", color: '#239a3b'},
                    ].map(({label, color}) => (
                        <div key={label} style={{
                            backgroundColor: color,
                            width: '20px',
                            height: '20px',
                            borderRadius: '4px',
                            marginLeft: '10px',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            boxShadow: '0 0 5px rgba(0, 0, 0, 0.2)',
                        }} title={label}></div>
                    ))}
                </div>
            </div>
            <div className="summary">
                <h3>학습 시간: {totalStudyTime} 분</h3>
                <h3>총 출석 일수: {totalAttendanceDays} 일</h3>
            </div>
        </div>
    );
};

const styles = `
    .yearly-study-table {
        padding: 20px;
        max-width: 100%; /* 가로 길이 늘리기 */
        margin: auto; /* 가운데 정렬 */
        background-color: white; /* 배경색 변경: 흰색 */
        border: 1px solid #e0e0e0; /* 테두리 색상 */
        border-radius: 8px; /* 모서리 둥글게 */
        box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
        position: relative; /* 자식 요소의 절대 위치 설정을 위한 relative 설정 */
        margin-bottom: 1rem;
    }
    h2 {
        display: inline-block; /* 제목과 버튼이 같은 라인에 배치되도록 설정 */
    }
    .year-navigation {
        display: flex;
        align-items: center; /* 화살표와 텍스트 정렬 */
        margin-left: 20px; /* 왼쪽 여백 추가 */
    }
    table {
        width: 100%; /* 100%로 설정하여 부모 요소의 너비에 맞춤 */
        border-collapse: separate; /* 선 없애기 위해 separate로 변경 */
        border-spacing: 0; /* 셀 사이의 간격을 없앴음 */
    }
    th, td {
        padding: 0; /* 패딩 제거 */
        text-align: center;
        height: 40px; /* 각 셀의 높이를 동일하게 설정 */
        width: 40px; /* 각 셀의 너비를 정사각형으로 설정 */
    }
    td.study-cell {
        transition: background-color 0.3s; /* 색상 변경 시 부드러운 전환 효과 */
    }
    td.study-cell:hover {
        opacity: 0.8; /* 마우스 오버 시 효과 */
    }
    .completion-summary {
        margin-top: 20px;
        display: flex;
        justify-content: space-between; /* 좌우 정렬 */
    }
    .arrow {
        cursor: pointer;
        padding: 10px; /* 클릭 영역 확대 */
        transition: transform 0.3s; /* 애니메이션 효과 추가 */
    }
    .arrow:hover {
        transform: scale(1.1); /* 마우스 오버 시 크기 확대 효과 */
    }
`;

const insertCSS = (css) => {
    const style = document.createElement('style');
    style.type = 'text/css';
    style.appendChild(document.createTextNode(css));
    document.head.appendChild(style);
};

insertCSS(styles);

export default YearlyStudyTable;
