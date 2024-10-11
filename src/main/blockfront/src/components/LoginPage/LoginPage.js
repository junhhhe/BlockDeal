import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from '../services/axiosConfig';  // 적절한 경로로 변경
import './LoginPage.css';

function LoginPage({ setIsAuthenticated }) {  // setIsAuthenticated prop을 추가로 받음
    const [formData, setFormData] = useState({ username: '', password: '' });
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    // 로그인 요청 처리 함수
    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');  // 오류 메시지 초기화
        setSuccessMessage('');  // 성공 메시지 초기화

        try {
            // 서버로 로그인 요청
            const response = await axios.post('/api/users/login', formData);

            // 서버 응답 구조 확인
            console.log('서버 응답:', response.data);

            const token = response.data.data;  // 응답에서 토큰 추출 (response.data가 바로 토큰)
            localStorage.setItem('token', token);  // 토큰을 로컬 스토리지에 저장

            setSuccessMessage('로그인 성공!');
            setIsAuthenticated(true);  // 로그인 상태 업데이트
            navigate('/');  // 메인 페이지로 리디렉션
        } catch (err) {
            console.error('로그인 오류:', err.response?.data?.message || err.message);
            setError('로그인 실패: 아이디 또는 비밀번호를 확인하세요.');
        }
    };

    const handleSignUp = () => {
        navigate('/signup');  // 회원가입 페이지로 이동
    };

    return (
        <div className="container">
            <div className="login-box">
                <h2>로그인</h2>
                {error && <p className="error-message">{error}</p>}
                <form onSubmit={handleSubmit}>
                    <input type="text" name="username" placeholder="아이디" value={formData.username} onChange={handleChange} required />
                    <input type="password" name="password" placeholder="비밀번호" value={formData.password} onChange={handleChange} required />
                    <button type="submit">로그인</button>
                </form>

                {/* 회원가입 버튼 추가 */}
                <button onClick={handleSignUp} style={{ marginTop: '10px', backgroundColor: '#f0f0f0' }}>회원가입</button>
            </div>
        </div>
    );
}

export default LoginPage;
