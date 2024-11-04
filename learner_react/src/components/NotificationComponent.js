import React, { useEffect } from 'react';

const NotificationComponent = () => {
    useEffect(() => {
        // 알림 권한 요청
        const requestNotificationPermission = async () => {
            if (Notification.permission === 'default') {
                const permission = await Notification.requestPermission();
                if (permission !== 'granted') {
                    console.error('알림 권한이 필요합니다.');
                }
            }
        };

        requestNotificationPermission();

        const newEventSource = new EventSource('http://localhost:8080/notifications/connect?memberId=1', {
            withCredentials: true
        });

        newEventSource.onmessage = (event) => {
            try {
                const parsedData = JSON.parse(event.data);
                // 메시지를 알림으로 표시
                showNotification(parsedData.message);
            } catch (error) {
                console.error("JSON 파싱 오류:", error);
            }
        };

        return () => {
            newEventSource.close(); // 컴포넌트 언마운트 시 연결 종료
        };
    }, []);

    const showNotification = (message) => {
        if (Notification.permission === 'granted') {
            new Notification('새 알림', {
                body: message,
            });
        }
    };

    return <div>알림을 수신 대기 중입니다...</div>;
};

export default NotificationComponent;
