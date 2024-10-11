import React from 'react';
import { Link } from 'react-router-dom';
import ImageCarousel from '../components/ImageCarousel';
import '../components/styles/Home.css';

const products = [
    { id: 1, image: 'T1.png', title: '검정 반팔 무지티', price: 12000, seller: '판매자1' },
    { id: 2, image: 'T2.jpg', title: '구찌(아구찜)티', price: 20000, seller: '판매자2' },
    { id: 3, image: 's1.jpg', title: '반스 검정 새신발', price: 70000, seller: '판매자3' },
    { id: 4, image: 'c.png', title: '피시방 의자 무료나눔', price: 0, seller: '판매자4' },
    { id: 5, image: 'T1.png', title: '상품5', price: 40000, seller: '판매자5' },
    { id: 6, image: 'T2.jpg', title: '상품6', price: 60000, seller: '판매자6' },
];

function Home() {
    return (
        <div className="home-container">
            <ImageCarousel images={['/path/to/Bimage1.png', '/path/to/Bimage2.png']} />
            <h3 className="section-title">오늘의 상품</h3>
            <div className="product-grid">
                {products.map(product => (
                    <div key={product.id} className="product-card">
                        <Link to={`/product/${product.id}`} className="product-link">
                            <img src={product.image} alt={product.title} className="product-image" />
                            <h4 className="product-title">{product.title}</h4>
                            <p className="product-price">{product.price.toLocaleString()}원</p>
                            <p className="product-seller">판매자: {product.seller}</p>
                        </Link>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default Home;
