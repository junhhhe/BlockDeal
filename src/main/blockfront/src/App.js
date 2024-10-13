import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Header from './components/Header'; // Header를 포함
import Home from './pages/Home';
import Mypage from './components/Mypage/Mypage';
import LoginPage from './components/LoginPage/LoginPage';
import SignUpPage from './components/LoginPage/SignUpPage'; // SignUpPage 추가
import CartPage from './components/Cart/CartPage';
import ChatPage from './components/Chat/ChatPage';  // 채팅 페이지 컴포넌트 추가
import ProductDetailPage from './components/Product/ProductDetail';  // 상품 상세 페이지
import Electronics from './components/Category/Electronics';  // 전자제품 페이지
import CategoryPage from './components/Category/CategoryPage';  // CategoryPage 컴포넌트를 불러옴
import AddProduct from './components/Product/AddProductPage';  // 상품 등록 페이지 추가
import ReviewForm from './components/Review/ReviewForm'; // 오타 수정 (ReviewPorm -> ReviewForm)
import ReviewList from './components/Review/ReviewList';
import ProductDetail from "./components/Product/ProductDetail";
import Footer from "./components/Footer/Footer";

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isAdmin, setIsAdmin] = useState(false);  // 관리자 여부 상태 추가

    // JWT 토큰 확인 및 로그인 상태 설정
    useEffect(() => {
        const token = localStorage.getItem('token');  // 로컬 스토리지에서 JWT 토큰 확인
        if (token) {
            setIsAuthenticated(true);  // 토큰이 있으면 로그인 상태로 설정

            // JWT 토큰에서 사용자 역할을 확인하여 관리자 여부 설정 (예시)
            const userRole = localStorage.getItem('role');  // 역할이 JWT나 로컬스토리지에 저장된 경우
            if (userRole === 'admin') {
                setIsAdmin(true);  // 관리자인 경우
            } else {
                setIsAdmin(false);  // 일반 사용자
            }
        }
    }, []);

    // 로그아웃 처리 함수
    const handleLogout = () => {
        localStorage.removeItem('token');  // JWT 토큰 삭제
        localStorage.removeItem('role');   // 역할 정보 삭제
        setIsAuthenticated(false);  // 로그인 상태 해제
        setIsAdmin(false);  // 관리자 상태 해제
    };

    return (
        <Router>
            <Header
                isAuthenticated={isAuthenticated}
                isAdmin={isAdmin}  // 관리자 상태 전달
                handleLogout={handleLogout}
            />
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={isAuthenticated ? <Navigate to="/" /> : <LoginPage setIsAuthenticated={setIsAuthenticated} />} />
                <Route path="/signup" element={<SignUpPage />} />  {/* 회원가입 페이지 */}
                <Route path="/mypage" element={isAuthenticated ? <Mypage /> : <Navigate to="/login" />} />
                <Route path="/cart" element={isAuthenticated ? <CartPage /> : <Navigate to="/login" />} />
                <Route path="/chat" element={isAuthenticated ? <ChatPage /> : <Navigate to="/login" />} /> {/* 채팅 경로 */}

                <Route path="/category/:categoryId" element={<CategoryPage />} />{/* 동적 카테고리 페이지 라우팅 설정 */}
                <Route path="/category/electronics" element={<Electronics />} /> {/* 전자제품 카테고리 페이지 */}
                <Route path="/product/:id" element={<ProductDetail />} />
                <Route path="/add-product" element={<AddProduct />} />  {/* 상품 등록 페이지 경로 추가 */}

                {/* 리뷰 작성 페이지 - 로그인한 사용자만 접근 가능 */}
                <Route path="/reviews/:sellerId" element={isAuthenticated ? <ReviewForm /> : <Navigate to="/login" />} />
                {/* 리뷰 목록 페이지 - 로그인하지 않아도 접근 가능 */}
                <Route path="/reviews/:sellerId/list" element={<ReviewList />} />
            </Routes>
            <Footer></Footer>
        </Router>
    );
}

export default App;
