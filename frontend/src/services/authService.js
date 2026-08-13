import api from './api';

export const authService = {
    signup:(fullName, email, password) => 
        api.post('/auth/signup', { fullName, email, password }),
    login:(email, password) => 
        api.post('/auth/login', { email, password }),
    logout:(email, password) =>{
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    },
    getCurrentUser: () => JSON.parse(localStorage.getItem('user')),
};