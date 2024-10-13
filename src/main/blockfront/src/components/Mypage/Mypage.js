import React, { useEffect, useState } from 'react';
import axios from '../services/axiosConfig';  // axios 설정 파일
import { useNavigate } from 'react-router-dom'; // useNavigate 사용
import './Mypage.css';

const Mypage = () => {
    const [user, setUser] = useState(null);
    const [error, setError] = useState('');
    const [wishlist, setWishlist] = useState([]);  // 찜 목록 상태
    const [products, setProducts] = useState([]);  // 등록한 상품 목록 상태
    const [reviews, setReviews] = useState([]);  // 작성한 후기 목록 상태
    const [profileImage, setProfileImage] = useState('');  // 프로필 이미지 상태
    const [loading, setLoading] = useState(true); // 로딩 상태
    const [activeTab, setActiveTab] = useState('products'); // 현재 선택된 탭
    const navigate = useNavigate(); // 페이지 이동을 위한 네비게이션 설정

    // 사용자 정보를 가져오는 함수
    const fetchUserData = async () => {
        try {
            const response = await axios.get('/api/users/info');
            const userData = response.data.data;
            setUser(userData);
            setWishlist(userData.wishlist || []); // 찜 목록 설정
            setProfileImage(userData.profileImage || 'https://via.placeholder.com/150');  // 기본 이미지 설정
        } catch (err) {
            setError('사용자 정보를 가져오는 데 실패했습니다.');
        } finally {
            setLoading(false); // 데이터 로딩 완료
        }
    };

    // 사용자가 등록한 상품 목록 가져오기
    const fetchUserProducts = async () => {
        try {
            const response = await axios.get('/api/products/my-products'); // 내가 등록한 상품 API 호출
            setProducts(response.data || []); // 상품 목록 설정 (없을 때 빈 배열)
        } catch (err) {
            setError('상품 목록을 가져오는 데 실패했습니다.');
        }
    };

    // 사용자가 작성한 후기 목록 가져오기
    const fetchUserReviews = async () => {
        try {
            const response = await axios.get('/api/reviews/my-reviews'); // 내가 작성한 후기 API 호출
            setReviews(response.data || []); // 후기 목록 설정 (없을 때 빈 배열)
        } catch (err) {
            setReviews([]);
            console.error('후기 목록을 가져오는 데 실패했습니다:', err);
        }
    };

    useEffect(() => {
        fetchUserData();
        fetchUserProducts(); // 마운트 시 사용자가 등록한 상품 목록 로드
        fetchUserReviews(); // 작성한 후기 목록 로드
    }, []);

    // 상품 등록 페이지로 이동하는 함수
    const goToProductRegister = () => {
        navigate('/register-product'); // 상품 등록 페이지로 이동
    };

    // 탭을 선택하는 함수
    const handleTabClick = (tab) => {
        setActiveTab(tab);  // 선택된 탭을 업데이트
    };

    if (loading) {
        return <div>로딩 중...</div>;
    }

    if (error) {
        return <div className="error-message">{error}</div>;
    }

    return (
        <div className="mypage-container">
            <ProfileSection user={user} profileImage={profileImage} />

            {/* 탭 메뉴 */}
            <div className="tab-menu">
                <button
                    className={`tab-link ${activeTab === 'products' ? 'active' : ''}`}
                    onClick={() => handleTabClick('products')}
                >
                    상품 {products.length}
                </button>
                <button
                    className={`tab-link ${activeTab === 'reviews' ? 'active' : ''}`}
                    onClick={() => handleTabClick('reviews')}
                >
                    상점후기 {reviews.length}
                </button>
                <button
                    className={`tab-link ${activeTab === 'wishlist' ? 'active' : ''}`}
                    onClick={() => handleTabClick('wishlist')}
                >
                    찜 {wishlist.length}
                </button>
                <button
                    className={`tab-link ${activeTab === 'following' ? 'active' : ''}`}
                    onClick={() => handleTabClick('following')}
                >
                    팔로잉 0
                </button>
                <button
                    className={`tab-link ${activeTab === 'follower' ? 'active' : ''}`}
                    onClick={() => handleTabClick('follower')}
                >
                    팔로워 0
                </button>
            </div>

            {/* 선택된 탭에 따라 섹션 렌더링 */}
            <div className="tab-content">
                {activeTab === 'products' && (
                    <ProductSection products={products} goToProductRegister={goToProductRegister} />
                )}
                {activeTab === 'reviews' && (
                    <ReviewSection reviews={reviews} />
                )}
                {activeTab === 'wishlist' && (
                    <WishlistSection wishlist={wishlist} />
                )}
                {activeTab === 'following' && (
                    <div>팔로잉 없음</div>
                )}
                {activeTab === 'follower' && (
                    <div>팔로워 없음</div>
                )}
            </div>
        </div>
    );
};

// 프로필 섹션 컴포넌트
const ProfileSection = ({ user, profileImage }) => (
    <div className="profile-section">
        <img src={profileImage} alt="프로필" className="profile-image" />
        <div className="profile-details">
            <h2>{user.name}</h2>
            <p className="email">이메일: {user.email}</p>
            <p className="nickname">닉네임: {user.nickname}</p>
        </div>
    </div>
);

// 찜한 목록 섹션 컴포넌트
const WishlistSection = ({ wishlist }) => (
    <div className="wishlist-section section-card">
        <h3>내가 찜한 목록</h3>
        {wishlist.length > 0 ? (
            <ul className="wishlist-list">
                {wishlist.map((item, index) => (
                    <WishlistItem key={index} item={item} />
                ))}
            </ul>
        ) : (
            <p>찜한 상품이 없습니다.</p>
        )}
    </div>
);

// 찜한 상품 항목 컴포넌트
const WishlistItem = ({ item }) => (
    <li className="wishlist-item">
        <img src={item.imageUrl} alt={item.title} className="wishlist-image" />
        <div className="wishlist-details">
            <p>{item.title}</p>
            <p>{item.price.toLocaleString()}원</p>
        </div>
    </li>
);

// 등록한 상품 섹션 컴포넌트 (그리드 레이아웃 적용)
const ProductSection = ({ products, goToProductRegister }) => (
    <div className="product-section section-card">
        <div className="section-header">
            <h3>내가 등록한 상품</h3>
        </div>
        {products.length > 0 ? (
            <div className="product-grid">
                {products.map((product, index) => (
                    <ProductItem key={index} product={product} />
                ))}
            </div>
        ) : (
            <p>등록한 상품이 없습니다.</p>
        )}
        <button onClick={goToProductRegister} className="product-register-button">상품 등록하기</button>
    </div>
);

// 등록한 상품 항목 컴포넌트
const ProductItem = ({ product }) => (
    <div className="product-item">
        {product.imageUrl ? (
            <div className="product-image">
                <img src={product.imageUrl} alt={product.title} />
            </div>
        ) : (
            <div className="product-image no-image">이미지 없음</div>
        )}
        <div className="product-details">
            <h4 className="product-title">{product.title}</h4>
            <p className="product-price">{product.price.toLocaleString()}원</p>
        </div>
    </div>
);

// 작성한 후기 섹션 컴포넌트
const ReviewSection = ({ reviews }) => (
    <div className="review-section section-card">
        <h3>내가 작성한 후기</h3>
        {reviews.length > 0 ? (
            <ul className="review-list">
                {reviews.map((review, index) => (
                    <ReviewItem key={index} review={review} />
                ))}
            </ul>
        ) : (
            <p>작성한 후기가 없습니다.</p>
        )}
    </div>
);

// 작성한 후기 항목 컴포넌트
const ReviewItem = ({ review }) => (
    <li className="review-item">
        <p><strong>{review.title}</strong></p>
        <p>{review.content}</p>
        <p>평점: {review.rating} / 5</p>
    </li>
);

export default Mypage;
