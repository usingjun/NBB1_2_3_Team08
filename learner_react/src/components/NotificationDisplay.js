import React from 'react';
import { useNotification } from '/NotificationComponent';

const NotificationDisplay = () => {
    const { messages } = useNotification();

    return (
        <div>
            <h2>Received Messages:</h2>
            {messages.map((msg, index) => (
                <div key={index}>{msg}</div>
            ))}
        </div>
    );
};

export default NotificationDisplay;
