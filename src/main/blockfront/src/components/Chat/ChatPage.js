import React, { useState, useEffect } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import axios from 'axios';
import './ChatPage.css';  // 스타일 파일 추가

const SOCKET_URL = "http://localhost:8001/ws"; // WebSocket 서버 URL

const ChatPage = ({ sellerUsername }) => {
    const [stompClient, setStompClient] = useState(null);
    const [message, setMessage] = useState("");
    const [chatMessages, setChatMessages] = useState([]);
    const [roomId, setRoomId] = useState(null);
    const [chatRooms, setChatRooms] = useState([]);
    const [recipient, setRecipient] = useState(sellerUsername);

    // WebSocket 연결 설정
    useEffect(() => {
        const token = localStorage.getItem('token');

        if (roomId) {
            const socket = new SockJS(SOCKET_URL);
            const client = new Client({
                webSocketFactory: () => socket,
                connectHeaders: {
                    Authorization: `Bearer ${token}`,
                },
                onConnect: (frame) => {
                    console.log("Connected: " + frame);

                    client.subscribe(`/sub/chat/${roomId}`, (msg) => {
                        const newMessage = JSON.parse(msg.body);
                        setChatMessages((prevMessages) => [...prevMessages, newMessage]);
                    });
                },
                onStompError: (error) => {
                    console.error("WebSocket error: ", error);
                },
            });

            client.activate();
            setStompClient(client);

            axios.get(`/api/chat/room/${roomId}/messages`)
                .then(response => {
                    setChatMessages(response.data);
                })
                .catch(error => {
                    console.error("Failed to load messages:", error);
                });

            return () => {
                client.deactivate();  // WebSocket 연결 해제
            };
        }
    }, [roomId]);

    // 참여 중인 채팅방 목록 불러오기
    useEffect(() => {
        axios.get('/api/chat/rooms')
            .then(response => {
                setChatRooms(response.data);
            })
            .catch(error => {
                console.error("Failed to load chat rooms:", error);
            });
    }, []);

    // 메시지 전송 함수
    const sendMessage = () => {
        if (stompClient && message.trim()) {
            const chatMessage = {
                roomId,
                message,
                recipient,
            };

            stompClient.publish({
                destination: "/pub/chat/send",
                body: JSON.stringify(chatMessage),
            });
            setMessage("");  // 메시지 전송 후 입력 필드 초기화
        }
    };

    // 채팅방 생성 및 접속 함수
    const startChatWithSeller = () => {
        const buyerUsername = localStorage.getItem('username');
        axios.post('/api/chat/room', { username1: buyerUsername, username2: recipient })
            .then(response => {
                setRoomId(response.data.id);  // 새로운 또는 기존 채팅방 ID 설정
            })
            .catch(error => {
                console.error("Failed to start chat with seller:", error);
            });
    };

    return (
        <div className="chat-page">
            {/* 채팅방 목록 */}
            <div className="chat-room-list">
                <h2>채팅방 목록</h2>
                {chatRooms.length > 0 ? (
                    <ul>
                        {chatRooms.map((room) => (
                            <li key={room.id} onClick={() => setRoomId(room.id)}>
                                채팅방 ID: {room.id} (참여자: {room.member1.username}, {room.member2.username})
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>현재 채팅방이 없습니다.</p>
                )}
            </div>

            {/* 판매자와 연결하기 버튼 */}
            {!roomId && (
                <button onClick={startChatWithSeller} className="start-chat-button">
                    판매자와 연결하기
                </button>
            )}

            {/* 채팅방 */}
            {roomId && (
                <div className="chat-room">
                    <h2>채팅방 ID - {roomId}</h2>

                    <div className="chat-messages">
                        {chatMessages.map((msg, index) => (
                            <div key={index} className="chat-message">
                                <strong>{msg.sender}</strong>: {msg.message}
                            </div>
                        ))}
                    </div>

                    <div className="chat-input-container">
                        <input
                            type="text"
                            value={message}
                            onChange={(e) => setMessage(e.target.value)}
                            placeholder="메시지를 입력하세요"
                            className="chat-input"
                        />
                        <button onClick={sendMessage} className="chat-send-button">전송</button>
                    </div>
                </div>
            )}

        </div>
    );
};

export default ChatPage;
