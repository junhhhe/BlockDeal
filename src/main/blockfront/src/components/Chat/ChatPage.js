import React, { useState, useEffect } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import axios from 'axios';

// WebSocket 서버 주소
const SOCKET_URL = "http://localhost:8001/ws"; // 실제 WebSocket 주소를 설정

const ChatPage = ({ sellerUsername }) => {
    const [stompClient, setStompClient] = useState(null);
    const [message, setMessage] = useState("");
    const [chatMessages, setChatMessages] = useState([]);
    const [roomId, setRoomId] = useState(null);  // 동적으로 채팅방 ID를 설정
    const [chatRooms, setChatRooms] = useState([]);
    const [recipient, setRecipient] = useState(sellerUsername); // 수신자 설정 (판매자 이름)

    // WebSocket 연결 설정
    useEffect(() => {
        const token = localStorage.getItem('token');  // JWT 토큰 가져오기

        if (roomId) {
            const socket = new SockJS(SOCKET_URL);
            const client = new Client({
                webSocketFactory: () => socket,  // SockJS 사용
                connectHeaders: {
                    Authorization: `Bearer ${token}`,  // JWT 토큰 헤더에 추가
                },
                onConnect: (frame) => {
                    console.log("Connected: " + frame);

                    // 특정 채팅방에 구독
                    client.subscribe(`/sub/chat/${roomId}`, (msg) => {
                        const newMessage = JSON.parse(msg.body);
                        setChatMessages((prevMessages) => [...prevMessages, newMessage]);
                    });
                },
                onStompError: (error) => {
                    console.error("WebSocket connection error: " + error);
                }
            });

            client.activate();  // WebSocket 활성화
            setStompClient(client);

            // 채팅방 메시지 로드
            axios.get(`/api/chat/room/${roomId}/messages`)
                .then(response => {
                    setChatMessages(response.data);
                })
                .catch(error => {
                    console.error("Failed to load messages:", error);
                });

            return () => {
                if (stompClient !== null) {
                    stompClient.deactivate();  // WebSocket 연결 해제
                }
            };
        }
    }, [roomId]); // roomId가 변경될 때마다 실행

    // 참여 중인 채팅방 목록을 불러오는 useEffect
    useEffect(() => {
        axios.get('/api/chat/rooms')
            .then(response => {
                setChatRooms(response.data);  // 채팅방 목록 설정
            })
            .catch(error => {
                console.error("Failed to load chat rooms:", error);
            });
    }, []);

    // 메시지 전송
    const sendMessage = () => {
        if (stompClient && message.trim()) {
            const chatMessage = {
                roomId: roomId,
                message: message,
                recipient: recipient // 수신자 설정
            };

            stompClient.publish({
                destination: "/pub/chat/send",
                body: JSON.stringify(chatMessage)
            });
            setMessage("");  // 전송 후 메시지 초기화
        }
    };

    // 채팅방 생성 및 접속 함수 (판매자와 채팅 시작)
    const startChatWithSeller = () => {
        const buyerUsername = localStorage.getItem('username'); // 현재 로그인한 사용자 이름
        axios.post('/api/chat/room', { username1: buyerUsername, username2: recipient })
            .then(response => {
                setRoomId(response.data.id); // 생성된 또는 기존의 채팅방 ID 설정
            })
            .catch(error => {
                console.error("Failed to start chat with seller:", error);
            });
    };

    return (
        <div>
            {/* 채팅방 목록 */}
            <div>
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
                <button onClick={startChatWithSeller}>
                    판매자와 연결하기
                </button>
            )}

            {/* 채팅방 */}
            {roomId && (
                <div>
                    <h2>채팅방 ID - {roomId}</h2>

                    <div style={{ border: "1px solid #ccc", padding: "10px", height: "300px", overflowY: "scroll" }}>
                        {chatMessages.map((msg, index) => (
                            <div key={index}>
                                <strong>{msg.sender}</strong>: {msg.message}
                            </div>
                        ))}
                    </div>

                    <input
                        type="text"
                        value={message}
                        onChange={(e) => setMessage(e.target.value)}
                        placeholder="메시지를 입력하세요"
                        style={{ width: "80%" }}
                    />
                    <button onClick={sendMessage} style={{ width: "20%" }}>전송</button>
                </div>
            )}
        </div>
    );
};

export default ChatPage;
