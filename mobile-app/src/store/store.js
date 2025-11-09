import { configureStore } from '@reduxjs/toolkit';
import authReducer from './slices/authSlice';
import reitsReducer from './slices/reitsSlice';
import portfolioReducer from './slices/portfolioSlice';

const store = configureStore({
  reducer: {
    auth: authReducer,
    reits: reitsReducer,
    portfolio: portfolioReducer,
  },
});

export default store;
