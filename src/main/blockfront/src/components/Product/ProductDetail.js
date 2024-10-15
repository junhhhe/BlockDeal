import React, { useEffect, useState, useContext } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import axios from '../../components/services/axiosConfig';
import AuthContext from '../../components/services/AuthContext';
import './ProductDetail.css';

const ProductDetail = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { user, loading: userLoading, error: userError } = useContext(AuthContext);
    const [product, setProduct] = useState(null);
    const [relatedProducts, setRelatedProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    // 상품 정보 API 호출
    const fetchProduct = async () => {
        try {
            const response = await axios.get(`/api/products/${id}`);
            setProduct(response.data);
        } catch (err) {
            console.error('상품 정보 API 호출 오류:', err);
            setError('상품 정보를 불러오는 데 실패했습니다.');
        }
    };

    // 연관 상품 API 호출
    const fetchRelatedProducts = async () => {
        try {
            const response = await axios.get(`/api/products/${id}/related`);
            setRelatedProducts(response.data);
        } catch (err) {
            console.error('연관 상품 API 호출 오류:', err);
            setError('연관 상품을 불러오는 데 실패했습니다.');
        }
    };

    // 찜하기 버튼 클릭 시 호출
    const handleAddToWishlist = async () => {
        const token = localStorage.getItem('token');

        if (!token) {
            alert('로그인이 필요합니다.');
            navigate('/login');
            return;
        }

        if (!product) {
            alert('유효한 상품 정보가 없습니다.');
            return;
        }

        setLoading(true);

        try {
            const requestBody = {
                productId: product.id,
            };

            await axios.post('/api/wishlist/add', requestBody, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            alert('상품이 찜 목록에 추가되었습니다.');
        } catch (error) {
            console.error('찜 목록 추가 오류:', error);

            // 서버로부터 이미 존재하는 경우에 대한 응답 처리
            if (error.response && error.response.status === 409) {
                alert('이미 찜한 상품입니다.');
            } else {
                setError('찜 목록에 추가하는 데 실패했습니다.');
            }
        } finally {
            setLoading(false);
        }
    };


    // 채팅 버튼 클릭 시 호출
    const handleStartChat = async () => {
        const token = localStorage.getItem('token');

        if (!token) {
            alert('로그인이 필요합니다.');
            navigate('/login');
            return;
        }

        if (!product || !user) {
            alert('유효한 사용자 또는 상품 정보가 없습니다.');
            return;
        }

        const buyerId = user?.id;
        const sellerId = product?.seller?.id;

        // 로그 출력 추가 - 올바른 정보가 출력되는지 확인
        console.log(`Logged in User (Buyer): ${JSON.stringify(user)}`);
        console.log(`Product Seller: ${JSON.stringify(product.seller)}`);
        console.log(`buyerId: ${buyerId}, sellerId: ${sellerId}, roomName: Chat_${user.username}_${product.seller.username}`);

        if (!buyerId || !sellerId) {
            alert('유효한 사용자 또는 판매자 정보가 없습니다.');
            return;
        }

        if (buyerId === sellerId) {
            alert('자기 자신과 채팅할 수 없습니다.');
            return;
        }

        try {
            setLoading(true);

            // 기존 채팅방 확인 및 생성은 모두 /api/chat/create에서 처리하도록 수정
            const response = await axios.post('/api/chat/create', {
                buyerId: buyerId,
                sellerId: sellerId,
                roomName: `Chat_${user.username}_${product.seller.username}`,
            }, {
                headers: { Authorization: `Bearer ${token}` }
            });

            const chatRoom = response.data.data;

            if (!chatRoom || !chatRoom.id) {
                throw new Error('채팅방 ID를 가져오지 못했습니다.');
            }

            navigate(`/chat/room/${chatRoom.id}`);
        } catch (error) {
            console.error('채팅방 생성 오류:', error);
            setError('채팅방을 생성하거나 가져오는 데 실패했습니다.');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        const loadData = async () => {
            setLoading(true);
            await fetchProduct();
            await fetchRelatedProducts();
            setLoading(false);
        };
        loadData();
    }, [id]);

    if (userLoading || loading) {
        return <div className="loading-spinner">로딩 중...</div>;
    }

    if (userError) {
        return <div className="error-message">사용자 정보를 불러오는 데 실패했습니다.</div>;
    }

    if (error) {
        return <div className="error-message">{error}</div>;
    }

    if (!product) {
        return <div>상품 정보를 불러오는 중입니다...</div>;
    }

    const isOwnProduct = user && product.seller && product.seller.username === user.username;

    return (
        <div className="product-detail-container">
            <div className="product-card">
                <div className="product-image-container">
                    <img src={product.imageUrl || '/default-image.png'} alt={product.title} className="product-image" />
                </div>
                <div className="vertical-line"></div>
                <div className="product-info">
                    <h2 className="product-title">{product.title}</h2>
                    <p className="product-price">{product.price.toLocaleString()}원</p>
                    <p className="product-description">{product.description}</p>
                    <p className="product-seller">판매자: {product.seller?.name || '정보 없음'}</p>
                    <div className="product-buttons">
                        <button className="action-button" onClick={handleAddToWishlist} disabled={loading}>
                            {loading ? '추가 중...' : '찜'}
                        </button>
                        <button className="action-button" onClick={handleStartChat} disabled={loading}>
                            연락
                        </button>
                        <Link to={`/purchase/${id}`} className="action-button buy-button">
                            바로 구매
                        </Link>
                        {isOwnProduct && (
                            <button className="action-button delete-button" onClick={() => navigate(`/delete-product/${id}`)}>
                                삭제하기
                            </button>
                        )}
                    </div>
                </div>
            </div>
            <div className="related-products">
                <h3>연관상품</h3>
                <div className="related-products-list">
                    {relatedProducts.length > 0 ? (
                        relatedProducts.map((relatedProduct) => (
                            <div key={relatedProduct.id} className="related-product-card" onClick={() => navigate(`/product/${relatedProduct.id}`)} style={{ cursor: 'pointer' }}>
                                <img src={relatedProduct.imageUrl || '/default-image.png'} alt={relatedProduct.title} className="related-product-image" />
                                <div className="related-product-info">
                                    <h4 className="related-product-title">{relatedProduct.title}</h4>
                                    <p className="related-product-description">{relatedProduct.description}</p>
                                </div>
                            </div>
                        ))
                    ) : (
                        <p>연관 상품이 없습니다.</p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ProductDetail;
