import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from '../components/services/axiosConfig'; // axios 설정 파일
import './styles/Header.css';

function Header({ onSearch, isAuthenticated, isAdmin, handleLogout }) {
    const [query, setQuery] = useState('');
    const [showSubCategories, setShowSubCategories] = useState(false); // 하위 카테고리 표시 상태
    const [searchHistory, setSearchHistory] = useState([]); // 검색 내역 저장
    const [showSearchHistory, setShowSearchHistory] = useState(false); // 검색 내역 표시 상태
    const [categories, setCategories] = useState([]); // 카테고리 목록 상태

    // 카테고리 목록을 백엔드에서 가져오는 함수
    const fetchCategories = async () => {
        try {
            const response = await axios.get('/api/products/categories');  // 카테고리 목록 API 호출
            setCategories(response.data);  // 가져온 카테고리 목록 저장
        } catch (error) {
            console.error('카테고리 정보를 가져오는 데 실패했습니다:', error);
        }
    };

    useEffect(() => {
        fetchCategories();  // 컴포넌트 로드 시 카테고리 목록 가져오기
    }, []);

    const handleSearch = (searchQuery) => {
        const searchValue = searchQuery || query;
        if (searchValue.trim()) {
            setSearchHistory([searchValue, ...searchHistory.filter(item => item !== searchValue)]); // 중복 제거 후 추가
            onSearch(searchValue); // 검색 함수 실행
            setShowSearchHistory(false); // 검색 후 내역 숨김
        }
    };

    const handleInputChange = (e) => {
        setQuery(e.target.value);
        setShowSearchHistory(true); // 검색어 입력 시 최근 검색어 표시
    };

    return (
        <header>
            <nav className="main-nav">
                <Link to="/" className="logo">
                    <img src='/BDLogo.png' alt="Logo" />
                </Link>
                <div className="search-bar">
                    <input
                        type="text"
                        placeholder="검색"
                        value={query}
                        onChange={handleInputChange}
                    />
                    <button onClick={() => handleSearch(query)}>검색</button>
                </div>
                <ul className="nav-links">
                    <li>
                        <Link to="/">홈</Link>
                    </li>
                    <li
                        className="category-menu"
                        onMouseEnter={() => setShowSubCategories(true)}
                        onMouseLeave={() => setShowSubCategories(false)}
                    >
                        <Link to="/category/1">카테고리</Link>
                        {showSubCategories && (
                            <ul className="sub-category">
                                {categories.length > 0 ? (
                                    categories.map((category) => (
                                        <li key={category.id}>
                                            <Link to={`/category/${category.id}`}>{category.name}</Link>
                                        </li>
                                    ))
                                ) : (
                                    <li>카테고리를 불러오는 중...</li>
                                )}
                            </ul>
                        )}
                    </li>
                    <li>
                        {/* 채팅 페이지 경로를 `/chatlist`로 변경 */}
                        <Link to="/chatlist">채팅</Link>
                    </li>
                    <li>
                        <Link to="/mypage">마이페이지</Link>
                    </li>
                    <li>
                        <Link to="/cart">장바구니</Link>
                    </li>

                    {isAuthenticated ? (
                        <>
                            <li>
                                <button onClick={handleLogout}>로그아웃</button>
                            </li>
                            {isAdmin && (
                                <li>
                                    <Link to="/admin">관리자 페이지</Link>
                                </li>
                            )}
                        </>
                    ) : (
                        <li>
                            <Link to="/login">로그인</Link>
                        </li>
                    )}
                </ul>
            </nav>
        </header>
    );
}

export default Header;
