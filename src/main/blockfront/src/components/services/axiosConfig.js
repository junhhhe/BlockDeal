import axios from 'axios';

const instance = axios.create({
    baseURL: 'http://localhost:8001',  // 백엔드 서버의 API 주소
});

instance.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');  // 로컬 스토리지에서 JWT 토큰 가져옴
        if (token) {
            console.log('토큰이 제대로 설정되고 있음:', token);  // 로깅 코드 추가
            config.headers.Authorization = `Bearer ${token}`;  // Authorization 헤더에 토큰 포함
        }
        return config;
    },
    error => Promise.reject(error)
);

export default instance;