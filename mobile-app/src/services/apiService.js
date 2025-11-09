import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

const API_BASE_URL = 'http://localhost:8080/api/v1';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor
apiClient.interceptors.request.use(
  async (config) => {
    const token = await AsyncStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      await AsyncStorage.removeItem('token');
      await AsyncStorage.removeItem('user');
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
  getTopDividendYielders: (limit) => apiClient.get(`/reits/top-dividend-yielders?limit=${limit}`),
};

export const portfolioService = {
  getPortfolio: () => apiClient.get('/portfolio'),
  refreshPortfolio: () => apiClient.post('/portfolio/refresh'),
};

export const transactionService = {
  buy: (data) => apiClient.post('/transactions/buy', data),
  sell: (data) => apiClient.post('/transactions/sell', data),
  getHistory: (page, size) => apiClient.get(`/transactions/history?page=${page}&size=${size}`),
};

export default apiClient;
