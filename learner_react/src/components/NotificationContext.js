import React, { createContext, useContext, useEffect, useState } from "react";

const NotificationContext = createContext();

export const NotificationProvider = ({ children }) => {
    const [notifications, setNotifications] = useState([]);
    const memberId = localStorage.getItem("memberId"); // 로컬 스토리지에서 memberId 가져오기

    useEffect(() => {
        const requestNotificationPermission = async () => {
            if ("Notification" in window) {
                const permission = await Notification.requestPermission();
                console.log("Notification permission status:", permission);
                if (permission !== "granted") {
                    console.warn("Notification permission denied");
                }
            }
        };

        requestNotificationPermission();
    }, []);

    useEffect(() => {
        if (!memberId) return; // memberId가 없으면 종료

        const eventSource = new EventSource(`http://localhost:8080/notifications/connect?memberId=${memberId}`, {
            withCredentials: true
        });

        eventSource.onopen = () => {
            console.log("SSE 연결 성공");
        };

        eventSource.onmessage = (event) => {
            console.log("일반 메시지 수신:", event.data);
        };

        eventSource.addEventListener("alarm", (event) => {
            console.log("알람 이벤트 수신:", event.data);
            try {
                const message = JSON.parse(event.data);
                setNotifications(prev => [...prev, message]);
                // 푸시 알림 생성
                if (Notification.permission === "granted") {
                    new Notification(message.title, {
                        body: message.content,
                        icon: 'icon.png' // 알림 아이콘 (선택사항)
                    });
                }
            } catch (error) {
                console.error("이벤트 데이터 파싱 오류:", error);
            }
        });

        eventSource.onerror = (error) => {
            console.error("SSE 연결 에러:", error);
            eventSource.close(); // 에러 발생 시 SSE 연결 닫기
        };

        return () => {
            eventSource.close(); // 컴포넌트 언마운트 시 SSE 연결 닫기
        };
    }, [memberId]);

    return (
        <NotificationContext.Provider value={{ notifications, setNotifications }}>
            {children}
        </NotificationContext.Provider>
    );
};

export const useNotification = () => {
    const context = useContext(NotificationContext);
    if (!context) {
        throw new Error("useNotification must be used within a NotificationProvider");
    }
    return context;
};

export default NotificationProvider;
