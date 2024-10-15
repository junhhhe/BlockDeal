import React, { useEffect, useState, useRef, useContext } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { useParams, useNavigate } from 'react-router-dom';
import axios from '../services/axiosConfig'; // axios import 추가
import AuthContext from '../services/AuthContext'; // 사용자 인증 정보 가져오기
import './ChatRoom.css'; // CSS 스타일 적용

function ChatRoom() {
    const { roomId } = useParams(); // URL에서 채팅방 ID 가져오기
    const navigate = useNavigate(); // 페이지 이동용 훅
    const { user } = useContext(AuthContext); // 사용자 정보 가져오기
    const [messages, setMessages] = useState([]); // 채팅 메시지 상태
    const [newMessage, setNewMessage] = useState(''); // 새 메시지 입력값
    const clientRef = useRef(null); // STOMP 클라이언트 참조
    const messagesEndRef = useRef(null); // 스크롤 참조용
    const token = localStorage.getItem('token'); // JWT 토큰 가져오기

    // 서버에서 기존 메시지 불러오기
    const fetchMessages = async () => {
        try {
            const response = await axios.get(`/api/chat/messages?roomId=${roomId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setMessages(response.data.data || []);
            scrollToBottom(); // 불러온 후 스크롤을 맨 아래로 이동
        } catch (error) {
            console.error('메시지 불러오기 오류:', error);
            alert('메시지를 불러오는 데 실패했습니다.');
        }
    };

    // WebSocket 연결 설정
    useEffect(() => {
        if (!token) {
            console.error('JWT 토큰이 없습니다. 로그인 페이지로 이동합니다.');
            navigate('/login'); // 토큰이 없으면 로그인 페이지로 이동
            return;
        }

        fetchMessages(); // 기존 메시지 불러오기

        const socketUrl = `http://localhost:8001/ws?token=${token}`; // WebSocket 경로에 JWT 쿼리 전달
        const socket = new SockJS(socketUrl); // SockJS 객체 생성

        const client = new Client({
            webSocketFactory: () => socket, // SockJS 사용
            connectHeaders: {
                Authorization: `Bearer ${token}`, // 헤더에 JWT 포함
            },
            debug: (str) => console.log(str), // 디버깅 로그
            reconnectDelay: 5000, // 재연결 지연 시간 설정
            onConnect: () => {
                console.log('STOMP 연결 성공');
                client.subscribe(`/sub/messages/${roomId}`, (message) => {
                    const newMessage = JSON.parse(message.body);
                    console.log('수신 메시지:', newMessage);
                    setMessages((prev) => [...prev, newMessage]); // 수신한 메시지 목록 업데이트
                    scrollToBottom(); // 스크롤 아래로 이동
                });
            },
            onStompError: (frame) => {
                console.error('STOMP 오류:', frame.headers['message']);
                alert('WebSocket 연결에 문제가 발생했습니다.');
            },
        });

        client.activate(); // STOMP 클라이언트 활성화
        clientRef.current = client; // 클라이언트 참조 저장

        return () => {
            client.deactivate(); // 컴포넌트 언마운트 시 클라이언트 비활성화
            console.log('STOMP 연결 해제');
        };
    }, [roomId, token]);

    // 스크롤을 맨 아래로 이동하는 함수
    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    // 메시지 전송 함수
    const sendMessage = () => {
        if (!newMessage.trim()) {
            alert('메시지를 입력하세요.');
            return;
        }

        const messagePayload = {
            chatRoomId: roomId,
            content: newMessage,
            senderName: user?.username || 'Unknown', // 사용자 이름 설정
            sentAt: new Date().toISOString(), // 전송 시간 기록
        };

        console.log('전송 메시지:', messagePayload);

        try {
            clientRef.current.publish({
                destination: `/pub/send/${roomId}`, // 메시지 발행 경로
                body: JSON.stringify(messagePayload), // 메시지 본문
                headers: {
                    Authorization: `Bearer ${token}`, // JWT 토큰 포함
                },
            });
            setNewMessage(''); // 메시지 입력 필드 초기화
            scrollToBottom(); // 메시지 전송 후 스크롤을 맨 아래로 이동
        } catch (error) {
            console.error('메시지 전송 오류:', error);
            alert('메시지 전송에 실패했습니다.');
        }
    };

    // 로딩 중 또는 사용자 정보가 없는 경우 처리
    if (!user) {
        return <div>로그인이 필요합니다. 로그인 페이지로 이동해주세요.</div>;
    }

    return (
        <div className="chat-room">
            <h1>채팅방: {roomId}</h1>
            <div className="messages-container">
                {messages.map((msg, index) => (
                    <div
                        key={index}
                        className={`message ${msg.senderName === user.username ? 'my-message' : ''}`}
                    >
                        <strong>{msg.senderName}</strong>: {msg.content}
                    </div>
                ))}
                <div ref={messagesEndRef} /> {/* 스크롤 끝 참조 */}
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
    );
}

export default ChatRoom;
