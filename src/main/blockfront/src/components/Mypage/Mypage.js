import React, { useEffect, useState } from 'react';
import axios from '../services/axiosConfig';  // axios 설정 파일
import { useNavigate } from 'react-router-dom'; // useNavigate 사용
import './Mypage.css';

const Mypage = () => {
    const [user, setUser] = useState(null);
    const [error, setError] = useState('');
    const [wishlist, setWishlist] = useState([]);  // 찜 목록 상태
    const [products, setProducts] = useState([]);  // 등록한 상품 목록 상태
    const [newPassword, setNewPassword] = useState('');  // 비밀번호 변경 상태
    const [profileImage, setProfileImage] = useState('');  // 프로필 이미지 상태
    const [loading, setLoading] = useState(true); // 로딩 상태
    const navigate = useNavigate(); // 페이지 이동을 위한 네비게이션 설정

    // 사용자 정보를 가져오는 함수
    const fetchUserData = async () => {
        try {
            const response = await axios.get('/api/users/info');
            const userData = response.data.data;
            setUser(userData);
            setWishlist(userData.wishlist || []); //찜목록 설정
            setProfileImage(userData.profileImage || 'https://via.placeholder.com/150');
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
            setProducts(response.data); // 상품 목록 설정
        } catch (err) {
            setError('상품 목록을 가져오는 데 실패했습니다.');
        }
    };

    // 비밀번호 변경 함수
    const handlePasswordChange = async () => {
        if (!newPassword) {
            alert('새 비밀번호를 입력해주세요.');
            return;
        }

        try {
            await axios.put('/api/users/update-password', { newPassword });
            alert('비밀번호가 성공적으로 변경되었습니다.');
            setNewPassword('');  // 비밀번호 입력란 초기화
        } catch (err) {
            setError('비밀번호 변경에 실패했습니다.');
        }
    };

    useEffect(() => {
        fetchUserData();
        fetchUserProducts(); // 마운트 시 사용자가 등록한 상품 목록 로드
    }, []);

    // 상품 등록 페이지로 이동하는 함수
    const goToProductRegister = () => {
        navigate('/register-product'); // 상품 등록 페이지로 이동
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
            <PasswordChangeSection
                newPassword={newPassword}
                onPasswordChange={setNewPassword}
                handlePasswordChange={handlePasswordChange}
            />
            <WishlistSection wishlist={wishlist} />
            <ProductSection products={products} goToProductRegister={goToProductRegister} /> {/* 등록한 상품 목록 섹션 */}
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


// 비밀번호 변경 섹션 컴포넌트
const PasswordChangeSection = ({ newPassword, onPasswordChange, handlePasswordChange }) => (
    <div className="password-change-section">
        <h3>비밀번호 변경</h3>
        <input
            type="password"
            placeholder="새 비밀번호"
            value={newPassword}
            onChange={(e) => onPasswordChange(e.target.value)}
        />
        <button onClick={handlePasswordChange}>비밀번호 변경</button>
    </div>
);

// 찜한 목록 섹션 컴포넌트
const WishlistSection = ({ wishlist }) => (
    <div className="wishlist-section">
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
        <img src={item.image} alt={item.name} className="wishlist-image" />
        <p>{item.name}</p>
    </li>
);

// 등록한 상품 섹션 컴포넌트
const ProductSection = ({ products, goToProductRegister }) => (
    <div className="product-section">
        <div className="section-header">
            <h3>내가 등록한 상품</h3>
            <button onClick={goToProductRegister} className="product-register-button">
                상품 등록
            </button>
        </div>
        {products.length > 0 ? (
            <ul className="product-list">
                {products.map((product, index) => (
                    <ProductItem key={index} product={product} />
                ))}
            </ul>
        ) : (
            <p>등록한 상품이 없습니다.</p>
        )}
    </div>
);

// 등록한 상품 항목 컴포넌트
const ProductItem = ({ product }) => (
    <li className="product-item">
        <img src={product.image} alt={product.name} className="product-image" />
        <p>{product.name}</p>
        <p>가격: {product.price}</p>
    </li>
);

export default Mypage;
