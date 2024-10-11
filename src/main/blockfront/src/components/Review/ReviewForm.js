import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function ReviewForm({ sellerId }) {
    const [rating, setRating] = useState(0);
    const [comment, setComment] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [isLoggedIn, setIsLoggedIn] = useState(false);  // 로그인 상태 관리
    const navigate = useNavigate();  // 리다이렉트를 위한 훅

    // 컴포넌트가 마운트될 때 로그인 상태 확인
    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            setIsLoggedIn(true);
        } else {
            setIsLoggedIn(false);
        }
    }, []);

    // 서버에 리뷰 제출
    const handleSubmit = async (e) => {
        e.preventDefault();

        if (rating < 1 || rating > 5) {
            setErrorMessage('별점은 1에서 5 사이여야 합니다.');
            return;
        }

        if (!comment.trim()) {
            setErrorMessage('리뷰 내용을 작성해야 합니다.');
            return;
        }

        setErrorMessage('');

        const reviewData = { rating, comment };

        try {
            const response = await fetch(`/api/reviews/${sellerId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,  // JWT 토큰 추가
                },
                body: JSON.stringify(reviewData),
            });

            if (response.ok) {
                alert('리뷰가 성공적으로 등록되었습니다.');
                setRating(0);
                setComment('');
            } else {
                const data = await response.json();
                setErrorMessage(data.message || '리뷰 작성에 실패했습니다.');
            }
        } catch (error) {
            setErrorMessage('서버와의 연결에 실패했습니다.');
        }
    };

    // 로그인 상태가 아니면 로그인 페이지로 리다이렉트
    useEffect(() => {
        if (!isLoggedIn) {
            alert('로그인이 필요합니다.');
            navigate('/login');  // 로그인 페이지로 리다이렉트
        }
    }, [isLoggedIn, navigate]);

    return (
        <div className="review-form">
            {isLoggedIn && (
                <>
                    <h2>리뷰 작성하기</h2>
                    {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
                    <form onSubmit={handleSubmit}>
                        <div>
                            <label>별점:</label>
                            <select
                                value={rating}
                                onChange={(e) => setRating(parseInt(e.target.value, 10))}
                            >
                                <option value={0}>별점을 선택하세요</option>
                                {[1, 2, 3, 4, 5].map((value) => (
                                    <option key={value} value={value}>
                                        {value}점
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div>
                            <label>리뷰:</label>
                            <textarea
                                value={comment}
                                onChange={(e) => setComment(e.target.value)}
                                placeholder="리뷰 내용을 입력하세요"
                            />
                        </div>
                        <button type="submit">리뷰 제출</button>
                    </form>
                </>
            )}
        </div>
    );
}

export default ReviewForm;
