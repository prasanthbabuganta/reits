import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  reits: [],
  selectedREIT: null,
  loading: false,
  error: null,
};

const reitsSlice = createSlice({
  name: 'reits',
  initialState,
  reducers: {
    setREITs: (state, action) => {
      state.reits = action.payload;
      state.loading = false;
    },
    setSelectedREIT: (state, action) => {
      state.selectedREIT = action.payload;
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

export const { setREITs, setSelectedREIT, setLoading, setError } = reitsSlice.actions;
export default reitsSlice.reducer;
