import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';

const ProductDetailPage = () => {
    const { productId } = useParams();
    const [product, setProduct] = useState(null);
    const [message, setMessage] = useState('');
    const [review, setReview] = useState('');
    const [rating, setRating] = useState(0);
    const [loading, setLoading] = useState(true); // 로딩 상태
    const [error, setError] = useState('');       // 에러 메시지 상태

    useEffect(() => {
        axios.get(`/api/products/${productId}`)
            .then(response => {
                setProduct(response.data);
                setLoading(false); // 데이터 로딩 완료 후 로딩 상태를 false로
            })
            .catch(error => {
                console.error(error);
                setError('상품 정보를 불러오는 데 실패했습니다.');
                setLoading(false);
            });
    }, [productId]);

    const addToWishlist = () => {
        axios.post(`/api/products/${productId}/wishlist`, {}, {
            headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
        })
            .then(response => alert(response.data))
            .catch(error => {
                console.error(error);
                setError('찜하기에 실패했습니다.');
            });
    };

    const connectWithSeller = () => {
        axios.post(`/api/products/chat/${productId}`, {}, {
            headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
        })
            .then(response => alert(response.data))
            .catch(error => {
                console.error(error);
                setError('판매자와 연결하는 데 실패했습니다.');
            });
    };

    const leaveReview = () => {
        // 평점은 1~5 사이로 제한
        if (rating < 1 || rating > 5) {
            alert("평점은 1에서 5 사이로 입력해주세요.");
            return;
        }

        axios.post(`/api/reviews/${product.seller.id}`, { content: review, rating }, {
            headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
        })
            .then(response => alert('리뷰가 성공적으로 등록되었습니다.'))
            .catch(error => {
                console.error(error);
                setError('리뷰 작성에 실패했습니다.');
            });
    };

    if (loading) return <div>로딩 중...</div>;

    if (error) return <div style={{ color: 'red' }}>{error}</div>;

    return (
        <div>
            <h2>{product.title}</h2>
            <p>{product.description}</p>
            <p>가격: {product.price} 원</p>

            <button onClick={addToWishlist}>찜하기</button>
            <button onClick={connectWithSeller}>판매자와 연결</button>

            {/* 리뷰 작성 */}
            <div>
                <h3>리뷰 남기기</h3>
                <textarea
                    value={review}
                    onChange={(e) => setReview(e.target.value)}
                    placeholder="리뷰를 작성하세요"
                    rows="4"
                    cols="50"
                />
                <br />
                <input
                    type="number"
                    value={rating}
                    onChange={(e) => setRating(e.target.value)}
                    placeholder="평점 (1~5)"
                    min="1"
                    max="5"
                />
                <button onClick={leaveReview}>리뷰 남기기</button>
            </div>
        </div>
    );
};

export default ProductDetailPage;
