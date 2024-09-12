import React, { useState } from 'react';
import './styles/ImageCarousel.css'; // 스타일 파일 import

const ImageCarousel = () => {
    const [currentIndex, setCurrentIndex] = useState(0);

    // 이미지 배열 정의
    const images = [
        '/Bimage1.png', // 실제 이미지 경로로 수정 필요
        '/Bimage2.png'  // 실제 이미지 경로로 수정 필요
    ];

    const handlePrev = () => {
        setCurrentIndex((prevIndex) =>
            prevIndex === 0 ? images.length - 1 : prevIndex - 1
        );
    };

    const handleNext = () => {
        setCurrentIndex((prevIndex) =>
            prevIndex === images.length - 1 ? 0 : prevIndex + 1
        );
    };

    return (
        <div className="image-carousel">
            <button className="prev" onClick={handlePrev}></button>
            <img src={images[currentIndex]} alt={`slide ${currentIndex}`} />
            <button className="next" onClick={handleNext}></button>
        </div>
    );
};

export default ImageCarousel;
