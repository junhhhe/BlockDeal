// src/components/Footer.js
import React from 'react';
import './Footer.css';

function Footer() {
  return (
      <footer className="footer-container">
        <div className="footer-content">
          <div className="footer-section">
            <h4>Block Deal</h4>
            <p>info@mysite.com</p>
            <p>Tel: 010-0000-0000</p>
          </div>
          <div className="footer-section">
            <h4>구매하기</h4>
            <ul>
              <li><a href="/">신제품</a></li>
              <li><a href="/">여성 의류</a></li>
              <li><a href="/">남성 의류</a></li>
            </ul>
          </div>
          <div className="footer-section">
            <h4>Block Deal</h4>
            <ul>
              <li><a href="/">브랜드 소개</a></li>
              <li><a href="/">구독</a></li>
              <li><a href="/">FAQ</a></li>
            </ul>
          </div>
          <div className="footer-section">
            <h4>이용약관</h4>
            <ul>
              <li><a href="/">쇼핑몰 정책</a></li>
              <li><a href="/">거래 방법</a></li>
              <li><a href="/">결제 방법</a></li>
            </ul>
          </div>
        </div>
        <div className="footer-bottom">
          <div className="footer-social-media">
            <a href="https://facebook.com"><i className="fab fa-facebook"></i></a>
            <a href="https://instagram.com"><i className="fab fa-instagram"></i></a>
            <a href="https://youtube.com"><i className="fab fa-youtube"></i></a>
            <a href="https://pinterest.com"><i className="fab fa-pinterest"></i></a>
            <a href="https://tiktok.com"><i className="fab fa-tiktok"></i></a>
          </div>
          <p>어떤 글을 쓸까 ??????? <a href="https://BlockDeal.com">BlockDeal.com</a></p>
        </div>
      </footer>
  );
}

export default Footer;
