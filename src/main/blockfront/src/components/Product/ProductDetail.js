import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import axios from '../services/axiosConfig'; // Axios 설정 파일 가져오기
import './ProductDetail.css';

const ProductDetail = () => {
    const { id } = useParams(); // URL에서 상품 ID를 가져옴
    const [product, setProduct] = useState(null); // 상품 데이터 상태
    const [relatedProducts, setRelatedProducts] = useState([]); // 연관 상품 상태
    const [error, setError] = useState(''); // 에러 상태

    // 상품 정보 API 호출
    const fetchProduct = async () => {
        try {
            const response = await axios.get(`/api/products/${id}`);
            console.log('Product data:', response.data); // product 객체 확인
            setProduct(response.data);
        } catch (err) {
            setError('상품 정보를 가져오는 데 실패했습니다.');
            console.error('상품 정보 API 호출 오류:', err);
        }
    };

    // 연관 상품 API 호출
    const fetchRelatedProducts = async () => {
        try {
            const response = await axios.get(`/api/products/${id}/related`); // 가정한 API 경로
            setRelatedProducts(response.data);
        } catch (err) {
            console.error('연관 상품 API 호출 오류:', err);
        }
    };

    // 컴포넌트가 마운트될 때 상품 및 연관 상품을 불러옴
    useEffect(() => {
        fetchProduct();
        fetchRelatedProducts();
    }, [id]);

    if (error) {
        return <div className="error-message">{error}</div>;
    }

    if (!product) {
        return <div>상품 정보를 불러오는 중입니다...</div>;
    }

    return (
        <div className="product-detail-container">
            <div className="product-card">
                <div className="product-image-container">
                    {/* imageUrl이 없는 경우 기본 이미지를 사용 */}
                    <img
                        src={product.imageUrl ? product.imageUrl : '/default-image.png'}
                        alt={product.title}
                        className="product-image"
                    />
                </div>
                <div className="vertical-line"></div>
                <div className="product-info">
                    <h2 className="product-title">{product.title}</h2>
                    <p className="product-price">{product.price.toLocaleString()}원</p>
                    <p className="product-description">{product.description}</p>
                    <p className="product-seller">판매자: {product.sellerName}</p>
                    <div className="product-buttons">
                        <button className="action-button">찜</button>
                        <button className="action-button">연락</button>
                        <Link to={`/purchase/${id}`} className="action-button buy-button">바로 구매</Link>
                    </div>
                </div>
            </div>

            <div className="related-products">
                <h3>연관상품</h3>
                <div className="related-products-list">
                    {relatedProducts.length > 0 ? (
                        relatedProducts.map(product => (
                            <div key={product.id} className="related-product-card">
                                <img src={product.imageUrl} alt={product.title} className="related-product-image" />
                                <div className="related-product-info">
                                    <h4 className="related-product-title">{product.title}</h4>
                                    <p className="related-product-description">{product.description}</p>
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
