import React, { useState, useEffect } from 'react';
import axios from '../../components/services/axiosConfig';  // axios 설정 파일
import { useNavigate } from 'react-router-dom';  // 페이지 이동을 위한 useNavigate
import ImageCarousel from '../../components/ImageCarousel';  // 이미지 캐러셀 컴포넌트
import './Category.css';

function Electronics() {
    const navigate = useNavigate();  // 페이지 이동을 위한 React Router 훅
    const [products, setProducts] = useState([]);  // 서버에서 가져온 제품 목록 상태
    const [sortOption, setSortOption] = useState('정확순');  // 정렬 옵션 상태
    const [error, setError] = useState(''); // 에러 상태

    // API 호출로 제품 목록을 가져오는 함수
    const fetchProducts = async () => {
        try {
            const response = await axios.get('/api/products/category/1/on-sale');  // 백엔드 API 호출
            setProducts(response.data);  // 가져온 데이터 상태에 저장
        } catch (err) {
            setError('상품 데이터를 가져오는 데 실패했습니다.');
            console.error('API 호출 중 오류가 발생했습니다:', err);
        }
    };

    useEffect(() => {
        fetchProducts();  // 페이지가 로드될 때 제품 목록 가져오기
    }, []);

    // 상품 클릭 시 상세 페이지로 이동하는 함수
    const handleClick = (id) => {
        navigate(`/product/${id}`);  // 해당 상품의 상세 페이지로 이동
    };

    // 정렬 버튼 클릭 시 정렬하는 함수
    const handleSort = (option) => {
        setSortOption(option);

        let sortedProducts = [...products];  // 기존 배열을 복사
        if (option === '고액순') {
            sortedProducts.sort((a, b) => b.price - a.price);  // 가격 높은 순으로 정렬
        } else if (option === '저액순') {
            sortedProducts.sort((a, b) => a.price - b.price);  // 가격 낮은 순으로 정렬
        } else if (option === '최신순') {
            sortedProducts.sort((a, b) => new Date(b.create) - new Date(a.create));  // 최신순으로 정렬
        }

        setProducts(sortedProducts);  // 정렬된 제품 상태로 업데이트
    };

    // 상품 등록 페이지로 이동하는 함수
    const handleAddProduct = () => {
        navigate('/add-product');  // 상품 등록 페이지로 이동
    };

    return (
        <div className="category">
            {/* 이미지 캐러셀 */}
            <ImageCarousel images={['/Bimage1.png', '/Bimage2.png']} />  {/* 캐러셀 이미지들 */}

            {/* 카테고리 제목 */}
            <h1 style={{ marginTop: '50px' }}>전자 제품</h1>

            {/* 상품 등록 버튼을 필터와 같은 형식으로 수정 */}
            <div className="filter-container">
                <p>정렬순: {sortOption}</p>

                {/* 필터 버튼들 */}
                <div className="filter-buttons">
                    <button onClick={() => handleSort('고액순')}>고액순</button>
                    <button onClick={() => handleSort('저액순')}>저액순</button>
                    <button onClick={() => handleSort('최신순')}>최신순</button>
                    <button onClick={() => handleSort('정확순')}>정확순</button>

                    {/* 상품 등록 버튼 추가 */}
                    <button className="category-add-product-button" onClick={handleAddProduct}>
                        상품 등록
                    </button>
                </div>
            </div>

            {/* 에러 메시지 표시 */}
            {error && <p className="error-message">{error}</p>}

            {/* 상품 목록 표시 */}
            <div className="category-container">
                {products.length > 0 ? (
                    products.map((product) => (
                        <div key={product.productId} className="category-item" onClick={() => handleClick(product.productId)}>
                            <img src={product.imageUrl} alt={product.title} className="category-image" />
                            <h2>{product.title}</h2>
                            <p>가격: {product.price.toLocaleString()}원</p>
                        </div>
                    ))
                ) : (
                    <p>등록된 상품이 없습니다.</p>
                )}
            </div>
        </div>
    );
}

export default Electronics;
