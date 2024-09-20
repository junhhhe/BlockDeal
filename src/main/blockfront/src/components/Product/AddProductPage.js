import React, { useState, useEffect } from 'react';
import axios from '../services/axiosConfig'; // axios 설정 파일
import { useNavigate } from 'react-router-dom';
import './AddProduct.css';

const AddProduct = () => {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [price, setPrice] = useState('');
    const [categoryId, setCategoryId] = useState('');
    const [categories, setCategories] = useState([]);
    const [imageFile, setImageFile] = useState(null);  // 이미지 파일 상태
    const [error, setError] = useState('');
    const navigate = useNavigate();

    // 카테고리 목록을 가져오는 함수
    const fetchCategories = async () => {
        try {
            const response = await axios.get('/api/products/categories');
            setCategories(response.data);  // 카테고리 목록 상태에 저장
        } catch (err) {
            console.error('카테고리 정보를 가져오는 데 실패했습니다.', err);
            setError('카테고리 정보를 가져오는 데 실패했습니다.');
        }
    };

    useEffect(() => {
        fetchCategories(); // 페이지가 로드될 때 카테고리 목록 가져오기
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData();  // FormData 객체 생성

        const productData = {
            title,
            description,
            price,
            category: { id: categoryId }  // 카테고리 ID만 Product에 넣기
        };

        formData.append('product', new Blob([JSON.stringify(productData)], { type: 'application/json' }));
        formData.append('imageFile', imageFile);

        try {
            // 서버로 상품 등록 요청 전송
            await axios.post('/api/products/add', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });

            alert('상품이 성공적으로 등록되었습니다.');
            navigate('/');  // 상품 등록 후 홈 페이지로 이동
        } catch (err) {
            console.error('상품 등록 중 오류가 발생했습니다.', err.response);
            setError(`상품 등록 중 오류가 발생했습니다: ${err.response?.data || err.message}`);
        }
    };

    return (
        <div className="add-product-container">
            <h1>상품 등록</h1>
            <form className="add-product-form" onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="title">상품명:</label>
                    <input
                        type="text"
                        id="title"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="description">상품 설명:</label>
                    <textarea
                        id="description"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="price">가격:</label>
                    <input
                        type="number"
                        id="price"
                        value={price}
                        onChange={(e) => setPrice(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="category">카테고리:</label>
                    <select
                        id="category"
                        value={categoryId}
                        onChange={(e) => setCategoryId(e.target.value)}
                        required
                    >
                        <option value="">카테고리를 선택하세요</option>
                        {categories.map((category) => (
                            <option key={category.id} value={category.id}>
                                {category.name} {/* 카테고리 이름을 표시 */}
                            </option>
                        ))}
                    </select>
                </div>
                <div>
                    <label htmlFor="imageFile">이미지 첨부:</label>
                    <input
                        type="file"
                        id="imageFile"
                        onChange={(e) => setImageFile(e.target.files[0])}  // 파일 선택 시 상태 업데이트
                        required
                    />
                </div>
                <button type="submit">상품 등록</button>
                {error && <p className="error-message">{error}</p>}
            </form>
        </div>
    );
};

export default AddProduct;
