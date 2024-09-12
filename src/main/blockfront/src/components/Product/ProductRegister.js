import React, { useState } from 'react';
import axios from '../services/axiosConfig';

const ProductRegister = () => {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [price, setPrice] = useState('');
    const [category, setCategory] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.post('/api/products/register', {
                title,
                description,
                price,
                category
            });

            if (response.status === 200) {
                alert('상품이 성공적으로 등록되었습니다.');
                setTitle('');
                setDescription('');
                setPrice('');
                setCategory('');
            }
        } catch (error) {
            console.error('상품 등록 중 오류가 발생했습니다:', error);
            alert('상품 등록에 실패했습니다.');
        }
    };

    return (
        <div className="product-register">
            <h2>상품 등록</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="상품명"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                />
                <textarea
                    placeholder="상품 설명"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                />
                <input
                    type="text"
                    placeholder="가격"
                    value={price}
                    onChange={(e) => setPrice(e.target.value)}
                />
                <input
                    type="text"
                    placeholder="카테고리"
                    value={category}
                    onChange={(e) => setCategory(e.target.value)}
                />
                <button type="submit">등록</button>
            </form>
        </div>
    );
};

export default ProductRegister;
