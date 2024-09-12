// src/components/Filter.js
import React, { useState } from 'react';

const Filter = ({ onFilterChange }) => {
    const [priceRange, setPriceRange] = useState('');
    const [category, setCategory] = useState('');

    const handleFilterChange = () => {
        onFilterChange({
            priceRange,
            category
        });
    };

    return (
        <div className="filter">
            <label>
                가격 범위:
                <select value={priceRange} onChange={(e) => setPriceRange(e.target.value)}>
                    <option value="">전체</option>
                    <option value="low">0 - 50,000원</option>
                    <option value="medium">50,000 - 100,000원</option>
                    <option value="high">100,000원 이상</option>
                </select>
            </label>
            <label>
                카테고리:
                <select value={category} onChange={(e) => setCategory(e.target.value)}>
                    <option value="">전체</option>
                    <option value="electronics">전자기기</option>
                    <option value="fashion">패션</option>
                    <option value="furniture">가구</option>
                </select>
            </label>
            <button onClick={handleFilterChange}>필터 적용</button>
        </div>
    );
};

export default Filter;
