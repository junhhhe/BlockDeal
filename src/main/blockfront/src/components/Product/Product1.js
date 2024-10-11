// src/components/Product/Product1.js
import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/Detail.css';

// 연관상품 데이터 (예시)
const relatedProducts = [
    { id: 13, image: 'https://example.com/product13.jpg', title: '연관상품1', description: '연관상품1 설명' },
    { id: 14, image: 'https://example.com/product14.jpg', title: '연관상품2', description: '연관상품2 설명' },
    { id: 15, image: 'https://example.com/product15.jpg', title: '연관상품3', description: '연관상품3 설명' },
    { id: 16, image: 'https://example.com/product16.jpg', title: '연관상품4', description: '연관상품4 설명' },
    { id: 17, image: 'https://example.com/product17.jpg', title: '연관상품5', description: '연관상품5 설명' },
];

const Product1 = ({ image, title, price = 0, description, seller, id }) => {
    return (
        <div className="product-detail-container">
            <div className="product-card">
                <div className="product-image-container">
                    <Link to={`/product/${id}`}>
                        <img src={image} alt={title} className="product-image" />
                    </Link>
                </div>
                <div className="vertical-line"></div>
                <div className="product-info">
                    <h2 className="product-title">{title}</h2>
                    <p className="product-price">{price.toLocaleString()}원</p>
                    <p className="product-description">{description}</p>
                    <p className="product-seller">판매자: {seller}</p>
                    <div className="product-buttons">
                        <button className="action-button">찜</button>
                        <button className="action-button">연락</button>
                        <Link to={`/product/${id}`} className="action-button buy-button">바로 구매</Link>
                    </div>
                </div>
            </div>
            <div className="related-products">
                <h3>연관상품</h3>
                <div className="related-products-list">
                    {relatedProducts.map(product => (
                        <div key={product.id} className="related-product-card">
                            <img src={product.image} alt={product.title} className="related-product-image" />
                            <div className="related-product-info">
                                <h4 className="related-product-title">{product.title}</h4>
                                <p className="related-product-description">{product.description}</p>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default Product1;
