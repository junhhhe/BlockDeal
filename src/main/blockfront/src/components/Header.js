import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars, faTimes, faSearch } from '@fortawesome/free-solid-svg-icons';
import axios from '../components/services/axiosConfig';
import './styles/Header.css';

function Header({ onSearch, isAuthenticated, isAdmin, handleLogout }) {
    const [query, setQuery] = useState('');
    const [showSubCategories, setShowSubCategories] = useState(false);
    const [searchHistory, setSearchHistory] = useState([]);
    const [showSearchHistory, setShowSearchHistory] = useState(false);
    const [categories, setCategories] = useState([]);

    useEffect(() => {
        fetchCategories();
        const savedHistory = localStorage.getItem('searchHistory');
        if (savedHistory) {
            setSearchHistory(JSON.parse(savedHistory));
        }
    }, []);

    useEffect(() => {
        localStorage.setItem('searchHistory', JSON.stringify(searchHistory));
    }, [searchHistory]);

    const fetchCategories = async () => {
        try {
            const response = await axios.get('/api/products/categories');
            setCategories(response.data);
        } catch (error) {
            console.error('카테고리 정보를 가져오는 데 실패했습니다:', error);
        }
    };

    const handleSearch = (searchQuery) => {
        const searchValue = searchQuery || query;
        if (searchValue.trim()) {
            setSearchHistory(prev => [searchValue, ...prev.filter(item => item !== searchValue)].slice(0, 10));
            onSearch(searchValue);
            setShowSearchHistory(false);
            setQuery('');
        }
    };

    const handleInputChange = (e) => {
        setQuery(e.target.value);
        setShowSearchHistory(true);
    };

    const handleBlur = () => {
        setTimeout(() => setShowSearchHistory(false), 200);
    };

    const clearSearchHistory = () => {
        setSearchHistory([]);
        localStorage.removeItem('searchHistory');
    };

    const removeSearchItem = (item) => {
        setSearchHistory(prev => prev.filter(historyItem => historyItem !== item));
    };

    return (
        <header>
            <nav className="main-nav">
                <ul className="nav-links">
                    <li className="category-menu"
                        onMouseEnter={() => setShowSubCategories(true)}
                        onMouseLeave={() => setShowSubCategories(false)}
                    >
                        <button className="category-button">
                            <FontAwesomeIcon icon={faBars} />
                        </button>
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
                </ul>

                <div className="logo-search-container">
                    <Link to="/" className="logo">
                        <img src='/BDLogo.png' alt="Logo" />
                    </Link>
                    <div className="search-bar">
                        <input
                            type="text"
                            placeholder="검색"
                            value={query}
                            onChange={handleInputChange}
                            onFocus={() => setShowSearchHistory(true)}
                            onBlur={handleBlur}
                        />
                        <button onClick={() => handleSearch(query)}>
                            <FontAwesomeIcon icon={faSearch} />
                        </button>
                        {showSearchHistory && (
                            <div className="search-history-dropdown">
                                <div className="search-history-header">
                                    <span>최근 검색어</span>
                                    <button onClick={clearSearchHistory}>전체삭제</button>
                                </div>
                                <ul className="search-history-list">
                                    {searchHistory.length > 0 ? (
                                        searchHistory.map((item, index) => (
                                            <li key={index}>
                                                <span onClick={() => handleSearch(item)}>{item}</span>
                                                <FontAwesomeIcon
                                                    icon={faTimes}
                                                    onClick={(e) => {
                                                        e.stopPropagation();
                                                        removeSearchItem(item);
                                                    }}
                                                />
                                            </li>
                                        ))
                                    ) : (
                                        <li>최근 검색어가 없습니다</li>
                                    )}
                                </ul>
                            </div>
                        )}
                    </div>
                </div>

                <ul className="nav-links">
                    <li><Link to="/chat">채팅</Link></li>
                    <li><Link to="/mypage">마이페이지</Link></li>
                    {isAuthenticated ? (
                        <>
                            <li><Link to="#" onClick={handleLogout}>로그아웃</Link></li>
                            {isAdmin && <li><Link to="/admin">관리자 페이지</Link></li>}
                        </>
                    ) : (
                        <li><Link to="/login">로그인</Link></li>
                    )}
                </ul>
            </nav>
        </header>
    );
}

export default Header;
