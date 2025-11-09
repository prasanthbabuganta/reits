import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  portfolio: null,
  holdings: [],
  loading: false,
  error: null,
};

const portfolioSlice = createSlice({
  name: 'portfolio',
  initialState,
  reducers: {
    setPortfolio: (state, action) => {
      state.portfolio = action.payload;
      state.holdings = action.payload.holdings || [];
      state.loading = false;
    },
    setLoading: (state, action) => {
      state.loading = action.payload;
    },
    setError: (state, action) => {
      state.error = action.payload;
      state.loading = false;
    },
  },
});

export const { setPortfolio, setLoading, setError } = portfolioSlice.actions;
export default portfolioSlice.reducer;
