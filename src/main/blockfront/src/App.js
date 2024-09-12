import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Header from './components/Header'; // Header를 포함
import Home from './pages/Home';
import Mypage from './components/Mypage/Mypage';
import LoginPage from './components/LoginPage/LoginPage';
import SignUpPage from './components/LoginPage/SignUpPage'; // SignUpPage 추가
import CartPage from './components/Cart/CartPage';
import ChatPage from './components/Chat/ChatPage';  // 채팅 페이지 컴포넌트 추가
import ProductDetailPage from './components/Product/ProductDetailPage';  // 상품 상세 페이지

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    // JWT 토큰 확인 및 로그인 상태 설정
    useEffect(() => {
        const token = localStorage.getItem('token');  // 로컬 스토리지에서 JWT 토큰 확인
        if (token) {
            setIsAuthenticated(true);  // 토큰이 있으면 로그인 상태로 설정
        }
    }, []);

    // 로그아웃 처리 함수 (이제는 useNavigate를 사용하지 않고 단순히 상태 관리)
    const handleLogout = () => {
        localStorage.removeItem('token');  // JWT 토큰 삭제
        setIsAuthenticated(false);  // 로그인 상태 해제
        // 로그아웃 시 navigate는 Header 컴포넌트에서 처리
    };

    return (
        <Router>
            <Header isAuthenticated={isAuthenticated} handleLogout={handleLogout} />
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={isAuthenticated ? <Navigate to="/" /> : <LoginPage setIsAuthenticated={setIsAuthenticated} />} />
                <Route path="/signup" element={<SignUpPage />} />  {/* 회원가입 페이지 추가 */}
                <Route path="/mypage" element={isAuthenticated ? <Mypage /> : <Navigate to="/login" />} />
                <Route path="/cart" element={isAuthenticated ? <CartPage /> : <Navigate to="/login" />} />
                <Route path="/chat" element={isAuthenticated ? <ChatPage /> : <Navigate to="/login" />} /> {/* 채팅 경로 추가 */}
                <Route path="/products/:productId" element={<ProductDetailPage />} /> {/* 상품 상세 페이지 */}
            </Routes>
        </Router>
    );
}

export default App;
