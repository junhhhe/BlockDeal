// src/components/NavBar.js
import React from 'react';
import { Link } from 'react-router-dom';
import '../components/styles/NavBar.css';

const NavBar = () => {
    return (
        <nav className="navbar">
            <Link to="/">홈</Link>
            <Link to="/category">카테고리</Link>
            <Link to="/chat">채팅</Link>
            <Link to="/mypage">마이페이지</Link>
            <Link to="/login">로그인</Link>
        </nav>
    );
};

export default NavBar;
