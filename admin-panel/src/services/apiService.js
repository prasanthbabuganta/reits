import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api/v1';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Handle responses
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authService = {
  login: (credentials) => apiClient.post('/auth/login', credentials),
  register: (data) => apiClient.post('/auth/register', data),
  logout: () => apiClient.post('/auth/logout'),
};

export const reitService = {
  getAll: () => apiClient.get('/reits'),
  getById: (id) => apiClient.get(`/reits/${id}`),
  search: (query) => apiClient.get(`/reits/search?q=${query}`),
  create: (data) => apiClient.post('/admin/reits', data),
  update: (id, data) => apiClient.put(`/admin/reits/${id}`, data),
};

export const userService = {
  getAll: () => apiClient.get('/admin/users'),
  getPendingKyc: () => apiClient.get('/admin/users/pending-kyc'),
  verifyKyc: (userId) => apiClient.put(`/admin/users/${userId}/verify-kyc`),
};

export const adminService = {
  getStats: () => apiClient.get('/admin/stats'),
};

export default apiClient;
