import React, { useState } from 'react';
import axios from '../services/axiosConfig';
import { useNavigate } from 'react-router-dom';
import './SignUpPage.css'; // CSS 파일 추가

function SignUp() {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        passwordCheck: '',
        name: '',
        nickname: '',
        email: ''
    });

    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        // 비밀번호 일치 여부 확인
        if (formData.password !== formData.passwordCheck) {
            setError('비밀번호가 일치하지 않습니다.');
            return;
        }

        // 회원가입 요청
        axios.post('/api/users/join', formData)
            .then(response => {
                alert('회원가입 성공!');
                navigate('/login');  // 성공 시 로그인 페이지로 이동
            })
            .catch(err => {
                setError(err.response?.data?.message || '회원가입 실패');
            });
    };

    return (
        <div className="container">
            <div className="signup-box">
                <h2>회원가입</h2>
                {error && <p className="error-message">{error}</p>}
                <form onSubmit={handleSubmit}>
                    <input type="text" name="username" placeholder="아이디" onChange={handleChange} required/>
                    <input type="password" name="password" placeholder="비밀번호" onChange={handleChange} required/>
                    <input type="password" name="passwordCheck" placeholder="비밀번호 확인" onChange={handleChange} required/>
                    <input type="text" name="name" placeholder="이름" onChange={handleChange} required/>
                    <input type="text" name="nickname" placeholder="닉네임" onChange={handleChange} required/>
                    <input type="email" name="email" placeholder="이메일" onChange={handleChange} required/>
                    <button type="submit">회원가입</button>
                    {/* 회원가입 버튼 */}
                </form>
            </div>
        </div>
    );
}

export default SignUp;
