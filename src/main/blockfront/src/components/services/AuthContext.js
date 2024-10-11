import React, { createContext, useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode'; // 명명된 import 사용

const AuthContext = createContext(null); // 초기값을 null로 설정

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchUser = () => {
            const token = localStorage.getItem('token');
            if (token) {
                try {
                    const decodedUser = jwtDecode(token); // 명명된 함수 사용
                    console.log('Decoded User:', decodedUser); // 디버깅 로그
                    setUser(decodedUser);
                } catch (err) {
                    console.error('토큰 디코드 오류:', err);
                    setError('사용자 정보를 가져오는 데 실패했습니다.');
                }
            }
            setLoading(false);
        };

        fetchUser();
    }, []);

    console.log('AuthContext value:', { user, loading, error }); // 디버깅 로그

    return (
        <AuthContext.Provider value={{ user, loading, error }}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthContext;
