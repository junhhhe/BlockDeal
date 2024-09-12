import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import './styles/Header.css';  // CSS 파일 가져오기

function Header({ isAuthenticated, handleLogout, onSearch, onPriceFilterChange }) {
    const [query, setQuery] = useState('');
    const [priceRange, setPriceRange] = useState('');
    const [showSubCategories, setShowSubCategories] = useState(false); // 하위 카테고리 표시 상태
    const [searchHistory, setSearchHistory] = useState([]); // 검색 내역 저장
    const [showSearchHistory, setShowSearchHistory] = useState(false); // 검색 내역 표시 상태
    const [categories, setCategories] = useState([]); // DB에서 불러온 카테고리 목록

    // 검색 기능
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

    const handleFocus = () => {
        if (searchHistory.length > 0) {
            setShowSearchHistory(true);  // 포커스 시 검색 내역 표시
        }
    };

    const handleBlur = () => {
        setTimeout(() => setShowSearchHistory(false), 200); // 검색창 벗어나면 검색 내역 숨기기
    };

    const handleDeleteHistory = (index) => {
        setSearchHistory(searchHistory.filter((_, i) => i !== index)); // 특정 항목 삭제
    };

    const handleClearAllHistory = () => {
        setSearchHistory([]); // 모든 검색 내역 삭제
    };

    // 가격 필터 변경 핸들러
    const handlePriceFilterChange = (e) => {
        setPriceRange(e.target.value);
        onPriceFilterChange(e.target.value);  // 가격 범위 선택을 부모 컴포넌트로 전달
    };

    // DB에서 카테고리 목록 가져오기
    useEffect(() => {
        axios.get('/api/products/categories')  // 카테고리 데이터를 DB에서 가져옴
            .then((response) => {
                console.log("카테고리 응답 데이터:", response.data);  // 응답 데이터를 확인하는 로그 추가
                setCategories(response.data); // 서버에서 받은 카테고리 목록 저장
            })
            .catch((error) => {
                console.error('카테고리 목록을 불러오는 데 실패했습니다.', error);
            });
    }, []);

    return (
        <header>
            <nav className="main-nav">
                <Link to="/" className="logo">
                    <img src='/BDLogo.png' alt="Logo" />
                </Link>

                {/* 검색 바 */}
                <div className="search-bar">
                    <input
                        type="text"
                        placeholder="검색"
                        value={query}
                        onChange={handleInputChange}
                        onFocus={handleFocus}
                        onBlur={handleBlur}
                    />
                    <button onClick={() => handleSearch(query)}>검색</button>
                    {/* 검색 내역 표시 */}
                    {showSearchHistory && (
                        <div className="search-history">
                            {searchHistory.length > 0 ? (
                                <ul>
                                    {searchHistory.map((historyItem, index) => (
                                        <li key={index}>
                                            <span onClick={() => handleSearch(historyItem)}>
                                                {historyItem}
                                            </span>
                                            <button onClick={() => handleDeleteHistory(index)}>
                                                삭제
                                            </button>
                                        </li>
                                    ))}
                                    <div className="clear-history">
                                        <button onClick={handleClearAllHistory}>전체 삭제</button>
                                    </div>
                                </ul>
                            ) : (
                                <div className="no-history">최근 검색어가 없습니다.</div>
                            )}
                        </div>
                    )}
                </div>

                {/* 가격 필터 */}
                <div className="price-filter">
                    <select value={priceRange} onChange={handlePriceFilterChange}>
                        <option value="">가격 범위</option>
                        <option value="low">0 - 50,000원</option>
                        <option value="medium">50,000 - 100,000원</option>
                        <option value="high">100,000원 이상</option>
                    </select>
                </div>

                {/* 네비게이션 링크 */}
                <ul className="nav-links">
                    <li>
                        <Link to="/">홈</Link>
                    </li>
                    <li
                        className="category-menu"
                        onMouseEnter={() => setShowSubCategories(true)}
                        onMouseLeave={() => setShowSubCategories(false)}
                    >
                        <Link to="#">카테고리</Link>
                        {showSubCategories && (
                            <ul className="sub-category">
                                {/* DB에서 가져온 카테고리 목록을 보여줌 */}
                                {categories.length > 0 ? (
                                    categories.map((category) => (
                                        <li key={category.id}>
                                            <Link to={`/category/${category.id}`}>
                                                {category.name}
                                            </Link>
                                        </li>
                                    ))
                                ) : (
                                    <li>카테고리가 없습니다.</li>
                                )}
                            </ul>
                        )}
                    </li>
                    <li>
                        <Link to="/chat">채팅</Link>
                    </li>

                    {/* 로그인 상태일 때만 마이페이지, 장바구니 접근 가능 */}
                    {isAuthenticated && (
                        <>
                            <li>
                                <Link to="/mypage">마이페이지</Link>
                            </li>
                            <li>
                                <Link to="/cart">장바구니</Link>
                            </li>
                        </>
                    )}

                    {/* 로그인 상태에 따라 로그인/로그아웃 버튼 표시 */}
                    {isAuthenticated ? (
                        <li>
                            <button onClick={handleLogout}>로그아웃</button>
                        </li>
                    ) : (
                        <li>
                            <Link to="/login">로그인</Link>
                        </li>
                    )}

                    {/* 관리자 페이지 */}
                    <li>
                        <Link to="/admin">관리자 페이지</Link>
                    </li>
                </ul>
            </nav>
        </header>
    );
}

export default Header;
