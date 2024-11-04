import React, { createContext, useContext, useEffect, useState } from 'react';

const NotificationContext = createContext();

export const useNotification = () => {
    return useContext(NotificationContext);
};

export const NotificationProvider = ({ children }) => {
    const [messages, setMessages] = useState([]);
    const [eventSource, setEventSource] = useState(null);
    const notification_Url = "http://localhost:8080/notifications";

    useEffect(() => {
        const storedMemberId = localStorage.getItem('memberId');
        if (storedMemberId) {
            const id = parseInt(storedMemberId, 10);
            if (!isNaN(id)) {
                startSseConnection(id);
            }
        }

        return () => {
            if (eventSource) {
                eventSource.close();
            }
        };
    }, []);

    const startSseConnection = (id) => {
        if (eventSource) {
            eventSource.close();
        }

        const newEventSource = new EventSource(`${notification_Url}/connect?memberId=${id}`, {
            withCredentials: true,
        });

        newEventSource.onmessage = (event) => {
            const parsedData = JSON.parse(event.data);
            setMessages((prev) => [...prev, parsedData.message]);
            showNotification(parsedData.message);
        };

        newEventSource.onerror = () => {
            console.error('SSE connection error');
            newEventSource.close();
        };

        setEventSource(newEventSource);
    };

    const showNotification = (message) => {
        if ('Notification' in window && Notification.permission === 'granted') {
            new Notification('새 알림', { body: message });
        } else if (Notification.permission !== 'denied') {
            Notification.requestPermission().then(permission => {
                if (permission === 'granted') {
                    new Notification('새 알림', { body: message });
                }
            });
        }
    };

    return (
        <NotificationContext.Provider value={{ messages }}>
            {children}
        </NotificationContext.Provider>
    );
};
