import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, createTheme, CssBaseline } from '@mui/material';
import Layout from './components/Layout';
import Dashboard from './components/Dashboard/Dashboard';
import REITList from './components/REITManagement/REITList';
import REITEditor from './components/REITManagement/REITEditor';
import UserList from './components/UserManagement/UserList';
import Login from './components/Auth/Login';
import PrivateRoute from './components/PrivateRoute';

const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route element={<PrivateRoute><Layout /></PrivateRoute>}>
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/reits" element={<REITList />} />
          <Route path="/reits/new" element={<REITEditor />} />
          <Route path="/reits/edit/:id" element={<REITEditor />} />
          <Route path="/users" element={<UserList />} />
        </Route>
      </Routes>
    </ThemeProvider>
  );
}

export default App;
