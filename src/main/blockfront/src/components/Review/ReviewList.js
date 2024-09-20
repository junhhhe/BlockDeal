import React, { useEffect, useState } from 'react';

function ReviewList({ sellerId }) {
    const [reviews, setReviews] = useState([]);
    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState('');

    // 서버에서 리뷰 목록 가져오기
    useEffect(() => {
        const fetchReviews = async () => {
            try {
                const response = await fetch(`/api/reviews/${sellerId}`);
                if (response.ok) {
                    const data = await response.json();
                    setReviews(data);
                } else {
                    setErrorMessage('리뷰를 불러오는 데 실패했습니다.');
                }
            } catch (error) {
                setErrorMessage('서버와의 연결에 실패했습니다.');
            } finally {
                setLoading(false);
            }
        };

        fetchReviews();
    }, [sellerId]);

    if (loading) {
        return <p>로딩 중...</p>;
    }

    if (errorMessage) {
        return <p style={{ color: 'red' }}>{errorMessage}</p>;
    }

    return (
        <div className="review-list">
            <h2>판매자에 대한 리뷰</h2>
            {reviews.length === 0 ? (
                <p>리뷰가 없습니다.</p>
            ) : (
                <ul>
                    {reviews.map((review) => (
                        <li key={review.id}>
                            <p>별점: {review.rating} / 5</p>
                            <p>리뷰: {review.comment}</p>
                            <p>작성일: {new Date(review.createdAt).toLocaleDateString()}</p>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default ReviewList;
