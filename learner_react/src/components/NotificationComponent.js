// import React, { createContext, useContext, useState, useEffect } from 'react';
//
// const NotificationContext = createContext();
//
// export const useNotification = () => {
//     const context = useContext(NotificationContext);
//     if (!context) {
//         throw new Error('useNotification must be used within a NotificationProvider');
//     }
//     return context;
// };
//
// const NotificationProvider = ({ children }) => {
//     const [notifications, setNotifications] = useState([]);
//
//     useEffect(() => {
//         const memberId = parseInt(localStorage.getItem('memberId'), 10);
//
//         if (memberId) {
//             const eventSource = new EventSource(`http://localhost:8080/notifications/connect?memberId=${memberId}`);
//
//             eventSource.onopen = () => {
//                 console.log('SSE 연결 성공');
//             };
//
//             eventSource.onmessage = (event) => {
//                 console.log('일반 메시지 수신:', event.data);
//             };
//
//             eventSource.addEventListener('alarm', (event) => {
//                 console.log('알람 이벤트 수신:', event.data);
//                 try {
//                     const { title, content } = JSON.parse(event.data);
//                     addNotification(title, content);
//                 } catch (error) {
//                     console.error('이벤트 데이터 파싱 오류:', error);
//                 }
//             });
//
//             eventSource.onerror = (error) => {
//                 console.error('SSE 연결 에러:', error);
//                 // 재연결 로직 추가 가능
//             };
//
//             return () => {
//                 eventSource.close();
//             };
//         }
//     }, []);
//
//     const addNotification = (title, message) => {
//         setNotifications((prev) => [...prev, { title, message }]);
//         showPushNotification(title, message);
//     };
//
//     const showPushNotification = (title, content) => {
//         if (Notification.permission === 'granted') {
//             new Notification(title, { body: content });
//         }
//     };
//
//     return (
//         <NotificationContext.Provider value={{ notifications, useNotification }}>
//             {children}
//         </NotificationContext.Provider>
//     );
// };
//
// export default NotificationProvider;