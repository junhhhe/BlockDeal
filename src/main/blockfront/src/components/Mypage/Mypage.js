import React, { useEffect, useState } from 'react';
import axios from '../services/axiosConfig'; // axios 설정 파일
import { useNavigate } from 'react-router-dom'; // 페이지 이동용 네비게이션
import './Mypage.css'; // 스타일 파일

const Mypage = () => {
    const [user, setUser] = useState(null);
    const [error, setError] = useState('');
    const [wishlist, setWishlist] = useState([]); // 찜 목록 상태
    const [products, setProducts] = useState([]); // 등록한 상품 목록 상태
    const [profileImage, setProfileImage] = useState(''); // 프로필 이미지 상태
    const [loading, setLoading] = useState(true); // 로딩 상태
    const navigate = useNavigate(); // 페이지 이동 설정

    // 사용자 정보 가져오기
    const fetchUserData = async () => {
        try {
            const response = await axios.get('/api/users/info', {
                headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
            });
            const userData = response.data.data;
            setUser(userData);
            setProfileImage(userData.profileImage || 'https://via.placeholder.com/150');
        } catch (err) {
            setError('사용자 정보를 가져오는 데 실패했습니다.');
        } finally {
            setLoading(false); // 데이터 로딩 완료
        }
    };

    // 찜 목록 가져오기
    const fetchWishlist = async () => {
        try {
            const response = await axios.get('/api/wishlist', {
                headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
            });
            console.log('Wishlist Data:', response.data);
            setWishlist(response.data.data || []);
        } catch (err) {
            console.error('찜 목록을 가져오는 데 실패했습니다:', err);
            setError('찜 목록을 가져오는 데 실패했습니다.');
        }
    };

    // 내가 등록한 상품 목록 가져오기
    const fetchUserProducts = async () => {
        try {
            const response = await axios.get('/api/products/my-products', {
                headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
            });
            setProducts(response.data);
        } catch (err) {
            setError('상품 목록을 가져오는 데 실패했습니다.');
        }
    };

    // 찜한 상품 삭제 기능
    const handleRemoveFromWishlist = async (productId) => {
        const token = localStorage.getItem('token');

        if (!token) {
            alert('로그인이 필요합니다.');
            navigate('/login');
            return;
        }

        try {
            await axios.delete(`/api/wishlist/remove/${productId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            // 삭제 후 상태 업데이트
            setWishlist((prevWishlist) =>
                prevWishlist.filter((item) => item.id !== productId)
            );

            alert('상품이 찜 목록에서 삭제되었습니다.');
        } catch (error) {
            console.error('찜 목록 삭제 오류:', error);
            setError('찜 목록에서 삭제하는 데 실패했습니다.');
        }
    };

    // 상품 등록 페이지로 이동
    const goToProductRegister = () => {
        navigate('/add-product'); // 상품 등록 페이지로 이동
    };

    useEffect(() => {
        fetchUserData();
        fetchWishlist();
        fetchUserProducts();
    }, []);

    if (loading) return <div>로딩 중...</div>;

    if (error) return <div className="error-message">{error}</div>;

    return (
        <div className="mypage-container">
            <ProfileSection user={user} profileImage={profileImage} />
            <WishlistSection wishlist={wishlist} handleRemoveFromWishlist={handleRemoveFromWishlist} />
            <ProductSection products={products} goToProductRegister={goToProductRegister} />
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
const WishlistSection = ({ wishlist, handleRemoveFromWishlist }) => (
    <div className="wishlist-section">
        <h3>내가 찜한 목록</h3>
        {wishlist.length > 0 ? (
            <ul className="wishlist-list">
                {wishlist.map((item) => (
                    <WishlistItem key={item.id} item={item} handleRemoveFromWishlist={handleRemoveFromWishlist} />
                ))}
            </ul>
        ) : (
            <p>찜한 상품이 없습니다.</p>
        )}
    </div>
);

// 찜한 상품 항목 컴포넌트
const WishlistItem = ({ item, handleRemoveFromWishlist }) => (
    <li className="wishlist-item">
        <img src={item.imageUrl} alt={item.title} className="wishlist-image" />
        <div className="wishlist-details">
            <p>{item.title}</p>
            <p>{item.price.toLocaleString()}원</p>
            <button onClick={() => handleRemoveFromWishlist(item.id)} className="remove-button">
                삭제
            </button>
        </div>
    </li>
);

// 등록한 상품 섹션 컴포넌트
const ProductSection = ({ products, goToProductRegister }) => (
    <div className="product-section">
        <div className="section-header">
            <h3>내가 등록한 상품</h3>
        </div>
        {products.length > 0 ? (
            <ul className="product-list">
                {products.map((product) => (
                    <ProductItem key={product.id} product={product} />
                ))}
            </ul>
        ) : (
            <p>등록한 상품이 없습니다.</p>
        )}
        <button onClick={goToProductRegister} className="register-button">
            상품 등록하기
        </button>
    </div>
);

// 등록한 상품 항목 컴포넌트
const ProductItem = ({ product }) => (
    <li className="product-item">
        <img src={product.imageUrl} alt={product.title} className="product-image" />
        <div className="product-details">
            <p>{product.title}</p>
            <p>{product.price.toLocaleString()}원</p>
        </div>
    </li>
);

export default Mypage;
