import React from 'react';
import { Link } from 'react-router-dom';
import Product1 from '../components/Product/Product1';
import ImageCarousel from '../components/ImageCarousel';
import '../components/styles/Home.css'; 
import '../components/styles/ImageCarousel.css'; 

// 상품 데이터
const products = [
    { id: 1, image: 'T1.png', title: '검정 반팔 무지티', price: 12000, description: '설명1', seller: '판매자1', category: 'electronics' },
    { id: 2, image: 'T2.jpg', title: '구찌(아구찜)티', price: 20000, description: '설명2', seller: '판매자2', category: 'fashion' },
    { id: 3, image: 's1.jpg', title: '반스 검정 새신발', price: 70000, description: '설명3', seller: '판매자3', category: 'home' },
    { id: 4, image: 'c.png', title: '피시방 의자 무료나눔', price: 0, description: '설명4', seller: '판매자4', category: 'sports' },
    { id: 5, image: 'T1.png', title: '상품5', price: 40000, description: '설명5', seller: '판매자5', category: 'books' },
    { id: 6, image: 'https://example.com/product6.jpg', title: '상품6', price: 60000, description: '설명6', seller: '판매자6', category: 'toys' },
    { id: 7, image: 'https://example.com/product7.jpg', title: '상품7', price: 80000, description: '설명7', seller: '판매자7', category: 'electronics' },
    { id: 8, image: 'https://example.com/product8.jpg', title: '상품8', price: 90000, description: '설명8', seller: '판매자8', category: 'fashion' },
    { id: 9, image: 'https://example.com/product9.jpg', title: '상품9', price: 110000, description: '설명9', seller: '판매자9', category: 'home' },
    { id: 10, image: 'https://example.com/product10.jpg', title: '상품10', price: 120000, description: '설명10', seller: '판매자10', category: 'sports' },
    { id: 11, image: 'https://example.com/product11.jpg', title: '상품11', price: 130000, description: '설명11', seller: '판매자11', category: 'books' },
    { id: 12, image: 'https://example.com/product12.jpg', title: '상품12', price: 140000, description: '설명12', seller: '판매자12', category: 'toys' },
];

function Home() {
    // 4개씩 나누기
    const chunkArray = (arr, size) => {
        const result = [];
        for (let i = 0; i < arr.length; i += size) {
            result.push(arr.slice(i, i + size));
        }
        return result;
    };

    const productChunks = chunkArray(products, 4);

    return (
        <div>
            <ImageCarousel images={['/path/to/Bimage1.png', '/path/to/Bimage2.png']} />
            <div className="product-container">
                {productChunks.map((chunk, index) => (
                    <div key={index} className="product-section">
                        {index === 0 && <h3>오늘의 상품</h3>}
                        <div className="product-list">
                            {chunk.map(product => (
                                <div key={product.id} className="product-card">
                                    <Link to={`/product/${product.id}`}>
                                        <img src={product.image} alt={product.title} className="product-image" />
                                    </Link>
                                    <h4 className="product-title">{product.title}</h4>
                                    <p className="product-price">{product.price.toLocaleString()}원</p>
                                </div>
                            ))}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default Home;