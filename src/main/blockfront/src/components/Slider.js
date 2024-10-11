import React, { useState, useEffect } from 'react';
import { useMediaQuery } from 'react-responsive';
import './styles/Slider.css'; // 스타일을 위한 CSS 파일 import

const slides = [
  '/Bimage1.png',
  '/Bimage2.png'
 
];

function Slider() {
  const [currentSlide, setCurrentSlide] = useState(0);

  useEffect(() => {
    const slideInterval = setInterval(() => {
      setCurrentSlide(prevSlide => (prevSlide + 1) % slides.length);
    }, 3000); // 슬라이드 전환 간격 (3초)

    return () => clearInterval(slideInterval); // 컴포넌트 언마운트 시 타이머 정리
  }, []);

  // Media queries
  const isMobile = useMediaQuery({ query: '(max-width: 767px)' });
  const isTablet = useMediaQuery({ query: '(min-width: 768px) and (max-width: 1024px)' });

  return (
    <div className="slider">
      <img 
        src={slides[currentSlide]} 
        alt={`Slide ${currentSlide + 1}`} 
        className={isMobile ? 'mobile' : isTablet ? 'tablet' : 'desktop'}
      />
    </div>
  );
}

export default Slider;
