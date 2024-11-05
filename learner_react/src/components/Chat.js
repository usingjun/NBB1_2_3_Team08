import React, { useState, useEffect, useRef } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';  // 최신 STOMP.js에서 Client 클래스를 사용

const Chat = () => {
    const [username, setUsername] = useState('');  // 사용자 이름 상태
    const [message, setMessage] = useState('');  // 입력된 메시지 상태
    const [messages, setMessages] = useState([]);  // 수신된 메시지 목록 상태
    const [isConnected, setIsConnected] = useState(false);  // WebSocket 연결 상태
    const stompClient = useRef(null);  // STOMP 클라이언트 참조
    const messageAreaRef = useRef(null);  // 메시지 영역 참조

    // WebSocket 서버에 연결
    const connect = () => {
        // 1. STOMP 클라이언트 생성
        stompClient.current = new Client({
            webSocketFactory: () => new SockJS('http://localhost:8080/ws'),  // Spring Boot 서버의 절대 경로 사용
            reconnectDelay: 5000,  // 재연결 딜레이 (5초)
            heartbeatIncoming: 4000,  // 서버로부터의 heartbeat 주기 (4초)
            heartbeatOutgoing: 4000,  // 클라이언트에서 서버로의 heartbeat 주기 (4초)
        });

        // 2. 연결 성공 시 호출될 콜백 함수 설정
        stompClient.current.onConnect = (frame) => {
            console.log('Connected:', frame);
            onConnected();  // 연결 성공 시 실행할 함수 호출
        };

        // 3. 연결 실패 시 호출될 콜백 함수 설정
        stompClient.current.onStompError = (frame) => {
            console.error('Broker reported error:', frame.headers['message']);
            console.error('Additional details:', frame.body);
            onError(frame);  // 연결 실패 시 실행할 함수 호출
        };

        // 4. WebSocket 연결 시작 (헤더에 인증 토큰 추가)
        stompClient.current.connectHeaders = {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`,  // JWT 토큰을 헤더에 포함
        };

        stompClient.current.activate();  // 클라이언트 활성화 및 연결 시작
    };

    // WebSocket에 성공적으로 연결된 경우 호출
    const onConnected = () => {
        // 특정 주제('/sub/chat')에 대한 메시지를 구독하여 실시간으로 수신
        stompClient.current.subscribe('/sub/chat', onMessageReceived);

        // 서버로 메시지 발행 (채팅방에 입장했음을 알리는 메시지)
        stompClient.current.publish({
            destination: "/pub/chat/enter",  // 메시지를 보낼 경로
            body: JSON.stringify({ sender: username, content: 'JOIN' })  // 전송할 메시지 내용 (JSON 형식)
        });

        setIsConnected(true);  // 연결 상태를 true로 설정 (UI에서 연결 상태를 반영할 수 있음)
    };

    // WebSocket 연결 실패 시 호출되는 함수
    const onError = (error) => {
        console.error('Could not connect to WebSocket server. Please refresh this page to try again!', error);
    };

    // 메시지 전송 함수
    const sendMessage = (event) => {
        event.preventDefault();
        if (message.trim() && stompClient.current) {
            const chatMessage = { sender: username, content: message, type: 'CHAT'};
            stompClient.current.publish({
                destination: "/pub/chat/message",
                body: JSON.stringify(chatMessage)
            });
            setMessage('');  // 메시지 전송 후 입력 필드 초기화
        }
    };

    // 서버로부터 메시지를 수신했을 때 호출되는 함수
    const onMessageReceived = (payload) => {
        const receivedMessage = JSON.parse(payload.body);
        setMessages((prevMessages) => [...prevMessages, receivedMessage]);
    };

    // 메시지가 추가될 때마다 스크롤을 하단으로 이동시키는 효과 적용
    useEffect(() => {
        if (messageAreaRef.current) {
            messageAreaRef.current.scrollTop = messageAreaRef.current.scrollHeight;
        }
    }, [messages]);

    // 컴포넌트가 마운트될 때 WebSocket 연결 설정 및 언마운트 시 해제 처리
    useEffect(() => {
        connect();  // 컴포넌트가 마운트될 때 WebSocket 연결 시도

        return () => {
            if (stompClient.current) {
                stompClient.current.deactivate();  // 컴포넌트가 언마운트될 때 WebSocket 연결 해제
            }
        };
    }, []);

    return (
        <div>
            <h1>Chat Application</h1>

            {/* 채팅 메시지 리스트 */}
            <div ref={messageAreaRef} style={{ height: '300px', overflowY: 'auto', border: '1px solid black' }}>
                {messages.map((msg, index) => (
                    <div key={index}>
                        <strong>{msg.sender}: </strong>{msg.content}
                    </div>
                ))}
            </div>

            {/* 사용자 이름 입력 */}
            {!isConnected && (
                <div>
                    <input
                        type="text"
                        placeholder="Enter your username..."
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <button onClick={connect}>Connect</button>
                </div>
            )}

            {/* 메시지 입력 필드 */}
            {isConnected && (
                <form onSubmit={sendMessage}>
                    <input
                        type="text"
                        placeholder="Enter your message..."
                        value={message}
                        onChange={(e) => setMessage(e.target.value)}
                    />
                    <button type="submit">Send</button>
                </form>
            )}

            {!isConnected && <p>Connecting to chat...</p>}
        </div>
    );
};

export default Chat;