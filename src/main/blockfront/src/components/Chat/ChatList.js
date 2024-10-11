import React, { useEffect, useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom'; // useNavigate 훅을 추가
import axios from '../services/axiosConfig';
import AuthContext from '../services/AuthContext';
import './ChatList.css';

function ChatList() {
    const { user, loading } = useContext(AuthContext); // AuthContext에서 사용자 정보 가져오기
    const [chatRooms, setChatRooms] = useState([]); // 초기값을 빈 배열로 설정
    const navigate = useNavigate(); // useNavigate 훅 선언

    // 채팅방 목록을 가져오는 함수
    const fetchChatRooms = async () => {
        if (!user) return; // 사용자 정보가 없으면 API 호출하지 않음

        try {
            const response = await axios.get('/api/chat/rooms', {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`,
                },
            });
            console.log('채팅방 목록 API 응답:', response.data); // 응답 데이터 로그 추가

            // API 응답 구조에서 data 필드의 값을 가져옴
            const rooms = response.data.data; // response.data.data를 가져와야 함
            setChatRooms(rooms); // 가져온 데이터를 설정
        } catch (err) {
            console.error('채팅방 목록 API 호출 오류:', err);
            setChatRooms([]); // 오류 발생 시 빈 배열로 설정하여 안전하게 처리
        }
    };


    // 컴포넌트 마운트 시 채팅방 목록을 가져옴
    useEffect(() => {
        fetchChatRooms();
    }, [user]);

    // 로딩 중이거나 데이터가 없을 때의 처리
    if (loading) {
        return <div>로딩 중...</div>;
    }

    if (!user) {
        return <div>사용자 정보가 없습니다. 로그인을 해주세요.</div>;
    }

    if (chatRooms.length === 0) {
        return <div>참여한 채팅방이 없습니다.</div>;
    }

    // 채팅방 클릭 시 해당 채팅방으로 이동
    const handleRoomClick = (roomId) => {
        navigate(`/chat/room/${roomId}`); // 채팅방 ID를 포함한 경로로 이동
    };

    return (
        <div className="chat-list">
            <h2>참여한 채팅방 목록</h2>
            <ul>
                {chatRooms.map((room) => (
                    <li
                        key={room.id}
                        onClick={() => handleRoomClick(room.id)} // 클릭 시 해당 채팅방으로 이동
                        className="chat-room-item" // 스타일링을 위한 클래스 추가
                        style={{ cursor: 'pointer' }} // 마우스 커서가 포인터로 변경되도록 스타일 설정
                    >
                        {/* ChatRoomDto의 필드에 맞게 데이터 접근 */}
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
