import React, { useEffect, useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from '../services/axiosConfig'; // axios 설정 파일
import AuthContext from '../services/AuthContext'; // 인증 컨텍스트
import './ChatList.css'; // CSS 파일

function ChatList() {
    const { user, loading } = useContext(AuthContext); // 사용자 정보 가져오기
    const [chatRooms, setChatRooms] = useState([]); // 채팅방 목록 상태
    const navigate = useNavigate(); // 네비게이션 훅

    const fetchChatRooms = async () => {
        try {
            const token = localStorage.getItem('token'); // 토큰 가져오기
            console.log("사용자 토큰:", token); // 토큰 확인
            const response = await axios.get('/api/chat/rooms', {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            console.log('채팅방 목록 API 응답:', response.data);
            setChatRooms(response.data.data);
        } catch (error) {
            console.error('채팅방 목록 API 호출 오류:', error.response || error);
            if (error.response && error.response.status === 401) {
                alert('토큰이 만료되었습니다. 다시 로그인해 주세요.');
                localStorage.removeItem('token'); // 만료된 토큰 제거
                navigate('/login'); // 로그인 페이지로 리다이렉션
            }
        }
    };

    useEffect(() => {
        if (user) {
            fetchChatRooms(); // 사용자 정보가 있을 때만 API 호출
        }
    }, [user]);

    if (loading) return <div>로딩 중...</div>; // 로딩 상태 표시

    if (!user) return <div>사용자 정보가 없습니다. 로그인을 해주세요.</div>; // 사용자 정보 없을 때

    if (chatRooms.length === 0) return <div>참여한 채팅방이 없습니다.</div>; // 채팅방이 없을 때

    const handleRoomClick = (roomId) => {
        navigate(`/chat/room/${roomId}`); // 채팅방으로 이동
    };

    return (
        <div className="chat-list">
            <h2>참여한 채팅방 목록</h2>
            <ul>
                {chatRooms.map((room) => (
                    <li
                        key={room.id}
                        onClick={() => handleRoomClick(room.id)}
                        className="chat-room-item"
                        style={{ cursor: 'pointer' }}
                    >
                        <p>채팅방 이름: {room.roomName ?? '알 수 없음'}</p>
                        <p>사용자 1: {room.user1 ?? '정보 없음'}</p>
                        <p>사용자 2: {room.user2 ?? '정보 없음'}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default ChatList;
