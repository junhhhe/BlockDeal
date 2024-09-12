import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';

const CategoryPage = () => {
    const { categoryId } = useParams();
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        axios.get(`/api/products/category/${categoryId}`)
            .then((response) => {
                setProducts(response.data);
                setLoading(false);
            })
            .catch((err) => {
                setError('상품을 불러오는 데 실패했습니다.');
                setLoading(false);
            });
    }, [categoryId]);

    if (loading) {
        return <div>로딩 중...</div>;
    }

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <div>
            <h1>카테고리 상품 목록</h1>
            {products.length > 0 ? (
                <ul>
                    {products.map((product) => (
                        <li key={product.id}>
                            <h2>{product.title}</h2>
                            <p>가격: {product.price}원</p>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>이 카테고리에 등록된 상품이 없습니다.</p>
            )}
        </div>
    );
};

export default CategoryPage;
