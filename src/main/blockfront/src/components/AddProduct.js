import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/AddProduct.css';

const AddProduct = ({ onAddProduct }) => {
    const [title, setTitle] = useState('');
    const [price, setPrice] = useState('');
    const [image, setImage] = useState('');
    const [description, setDescription] = useState('');
    const [seller, setSeller] = useState('');

    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        const newProduct = {
            id: Date.now(), // 임시 ID 생성
            title,
            price: parseInt(price, 10),
            image,
            description,
            seller
        };

        onAddProduct(newProduct);
        navigate('/'); // 홈 화면으로 리다이렉트
    };

    return (
        <div className="add-product-container">
            <h2>상품 추가하기</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="title">상품명:</label>
                    <input
                        type="text"
                        id="title"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="price">가격:</label>
                    <input
                        type="number"
                        id="price"
                        value={price}
                        onChange={(e) => setPrice(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="image">이미지 URL:</label>
                    <input
                        type="text"
                        id="image"
                        value={image}
                        onChange={(e) => setImage(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="description">설명:</label>
                    <textarea
                        id="description"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                    ></textarea>
                </div>
                <div className="form-group">
                    <label htmlFor="seller">판매자:</label>
                    <input
                        type="text"
                        id="seller"
                        value={seller}
                        onChange={(e) => setSeller(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">상품 추가</button>
            </form>
        </div>
    );
};

export default AddProduct;
