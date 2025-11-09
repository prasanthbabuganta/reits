import { configureStore } from '@reduxjs/toolkit';
import authReducer from './slices/authSlice';
import reitsReducer from './slices/reitsSlice';
import usersReducer from './slices/usersSlice';

const store = configureStore({
  reducer: {
    auth: authReducer,
    reits: reitsReducer,
    users: usersReducer,
  },
});

export default store;
