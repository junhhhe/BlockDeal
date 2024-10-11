import React, { useEffect, useState, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import axios from '../services/axiosConfig';
import './ChatRoom.css';

function ChatRoom() {
    const { roomId } = useParams(); // URL에서 채팅방 ID 가져오기
    const navigate = useNavigate();
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const clientRef = useRef(null);
    const messagesEndRef = useRef(null);
    const token = localStorage.getItem('token'); // JWT 토큰 가져오기

    // WebSocket 연결 설정 및 메시지 수신 설정
    useEffect(() => {
        // WebSocket 설정 함수 호출
        connectStomp();

        return () => {
            if (clientRef.current) {
                clientRef.current.deactivate(); // 컴포넌트 언마운트 시 WebSocket 연결 해제
                console.log('STOMP 연결 해제');
            }
        };
    }, []);

    // WebSocket 연결 함수
    const connectStomp = () => {
        if (!token) {
            console.error('JWT 토큰이 없습니다. WebSocket 연결을 중단합니다.');
            return;
        }

        // SockJS와 STOMP 클라이언트 설정
        const socket = new SockJS('http://localhost:8001/ws'); // WebSocket 엔드포인트 설정
        const client = new Client({
            webSocketFactory: () => socket,
            connectHeaders: {
                Authorization: `Bearer ${token}`, // CONNECT 명령어에 JWT 토큰 포함
            },
            debug: (str) => console.log(str), // STOMP 디버그 로그 출력
            reconnectDelay: 5000, // 재연결 시도 시간 설정
            onConnect: () => {
                console.log('STOMP 연결 성공');

                // 채팅방 구독 설정
                client.subscribe(`/sub/messages/${roomId}`, (message) => {
                    if (message.body) {
                        const newMessage = JSON.parse(message.body);
                        console.log('받은 메시지:', newMessage);
                        setMessages((prevMessages) => [...prevMessages, newMessage]);
                    }
                });
            },
            onStompError: (frame) => {
                console.error('STOMP 오류:', frame.headers['message']);
            },
            onWebSocketClose: () => {
                console.log('WebSocket 연결이 닫혔습니다.');
            },
            onDisconnect: () => {
                console.log('STOMP 연결 종료');
            },
        });

        client.activate(); // WebSocket 연결 활성화
        clientRef.current = client; // 클라이언트 참조 설정
    };

    // 메시지 전송 함수
    const sendMessage = () => {
        if (clientRef.current && newMessage.trim() !== '') {
            const messagePayload = {
                content: newMessage,
                chatRoomId: roomId,
            };

            // 메시지 전송 시 헤더 확인
            console.log('전송되는 메시지 데이터:', messagePayload);
            console.log('Authorization 헤더:', `Bearer ${token}`);

            clientRef.current.publish({
                destination: `/pub/send`, // 메시지 전송 경로
                body: JSON.stringify(messagePayload),
                headers: {
                    Authorization: `Bearer ${token}`, // SEND 명령어에도 JWT 토큰 포함
                },
            });
            setNewMessage(''); // 메시지 전송 후 입력 필드 초기화
        }
    };

    return (
        <div className="chat-room-wrapper">
            <div className="chat-room">
                <div className="chat-room-title">채팅방: {roomId}</div>
                <div className="messages-container">
                    {messages.length > 0 ? (
                        messages.map((msg, index) => (
                            <div
                                key={index}
                                className={`message ${msg.sender === 'myself' ? 'my-message' : ''}`}
                            >
                                <strong>{msg.sender}</strong>: {msg.content}
                            </div>
                        ))
                    ) : (
                        <p>메시지가 없습니다.</p>
                    )}
                    <div ref={messagesEndRef}></div>
                </div>
                <div className="message-input">
                    <input
                        type="text"
                        value={newMessage}
                        onChange={(e) => setNewMessage(e.target.value)}
                        placeholder="메시지를 입력하세요..."
                    />
                    <button onClick={sendMessage}>전송</button>
                </div>
            </div>
        </div>
    );
}

export default ChatRoom;
