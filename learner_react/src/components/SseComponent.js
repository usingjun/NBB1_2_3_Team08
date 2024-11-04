import React, { useEffect, useState } from 'react';

const SseComponent = () => {
    const [messages, setMessages] = useState([]);
    const [eventSource, setEventSource] = useState(null);
    const [notificationMessage, setNotificationMessage] = useState('');
    const [memberId, setMemberId] = useState(null);
    const notification_Url = "http://localhost:8080/notifications";

    useEffect(() => {
        requestNotificationPermission();

        const storedMemberId = localStorage.getItem('memberId');
        if (storedMemberId) {
            const id = parseInt(storedMemberId, 10);
            if (!isNaN(id)) {
                setMemberId(id); // memberId 설정
                startSseConnection(id);
            } else {
                console.error('Invalid memberId:', storedMemberId);
            }
        }

        return () => {
            if (eventSource) {
                eventSource.close();
            }
        };
    }, []);

    const requestNotificationPermission = async () => {
        if ('Notification' in window) {
            const permission = await Notification.requestPermission();
            if (permission !== 'granted') {
                console.error('Notification permission denied');
            }
        }
    };

    const startSseConnection = (id) => {
        if (eventSource) {
            eventSource.close(); // 이전 연결 닫기
        }

        const newEventSource = new EventSource(`${notification_Url}/connect?memberId=${id}`, {
            withCredentials: true,
        });

        newEventSource.onmessage = (event) => {
            const parsedData = JSON.parse(event.data);
            setMessages((prev) => [...prev, `Message: ${parsedData.message}`]);
            showNotification(parsedData.message); // 푸시 알림 표시
        };

        newEventSource.onerror = () => {
            console.error('SSE connection error');
            newEventSource.close();
        };

        setEventSource(newEventSource);
    };

    const showNotification = (message) => {
        if ('Notification' in window && Notification.permission === 'granted') {
            new Notification('새 알림', {
                body: message,
                icon: 'path/to/icon.png', // 아이콘 경로 (옵션)
            });
        } else if (Notification.permission !== 'denied') {
            Notification.requestPermission().then(permission => {
                if (permission === 'granted') {
                    new Notification('새 알림', {
                        body: message,
                    });
                }
            });
        }
    };

    const sendNotification = async () => {
        if (memberId === null) {
            console.error('memberId is null');
            return; // memberId가 null일 때 함수 종료
        }

        const response = await fetch(`${notification_Url}/notify`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',
            body: JSON.stringify({ memberId: memberId, message: notificationMessage }), // memberId 사용
        });

        if (response.ok) {
            setNotificationMessage('');
        } else {
            const errorText = await response.text();
            console.error('알림 전송 실패:', errorText);
        }
    };

    return (
        <div>
            <h1>SSE Test Page</h1>
            <input
                type="text"
                value={notificationMessage}
                onChange={(e) => setNotificationMessage(e.target.value)}
                placeholder="Notification message"
            />
            <button onClick={sendNotification}>Send Notification</button>
            <div>
                <h2>Received Messages:</h2>
                {messages.map((msg, index) => (
                    <div key={index}>{msg}</div>
                ))}
            </div>
        </div>
    );
};

export default SseComponent;
