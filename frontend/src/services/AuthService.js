import api from '../api/axiosConfig';

const AuthService = {
    login: async (username, password) => {
        // Исправлено: отправляем identifier вместо username, так как этого ждет бэкенд
        const response = await api.post('/auth/login', { identifier: username, password });
        
        if (response.data.token) {
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('username', username);
        }
        return response.data;
    },

    register: async (userData) => {
        return await api.post('/auth/register', userData);
    },

    logout: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('username');
    },

    getCurrentUser: () => {
        return localStorage.getItem('username');
    },
    
    isAuthenticated: () => {
        return !!localStorage.getItem('token');
    }
};

export default AuthService;
