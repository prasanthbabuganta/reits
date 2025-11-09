import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import {
  Box,
  Button,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
  Chip,
} from '@mui/material';
import { CheckCircle } from '@mui/icons-material';
import { setUsers, setLoading } from '../../store/slices/usersSlice';
import { userService } from '../../services/apiService';

export default function UserList() {
  const dispatch = useDispatch();
  const { users, loading } = useSelector((state) => state.users);

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    dispatch(setLoading(true));
    try {
      const response = await userService.getAll();
      dispatch(setUsers(response.data));
    } catch (error) {
      console.error('Failed to load users:', error);
    }
  };

  const handleVerifyKyc = async (userId) => {
    try {
      await userService.verifyKyc(userId);
      loadUsers();
    } catch (error) {
      console.error('Failed to verify KYC:', error);
    }
  };

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        User Management
      </Typography>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Name</TableCell>
              <TableCell>Email</TableCell>
              <TableCell>Country</TableCell>
              <TableCell>KYC Status</TableCell>
              <TableCell>Account Status</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {loading ? (
              <TableRow>
                <TableCell colSpan={7} align="center">
                  Loading...
                </TableCell>
              </TableRow>
            ) : users.length === 0 ? (
              <TableRow>
                <TableCell colSpan={7} align="center">
                  No users found
                </TableCell>
              </TableRow>
            ) : (
              users.map((user) => (
                <TableRow key={user.id}>
                  <TableCell>{user.id}</TableCell>
                  <TableCell>
                    {user.firstName} {user.lastName}
                  </TableCell>
                  <TableCell>{user.email}</TableCell>
                  <TableCell>{user.countryCode || 'N/A'}</TableCell>
                  <TableCell>
                    <Chip
                      label={user.kycVerified ? 'Verified' : 'Pending'}
                      color={user.kycVerified ? 'success' : 'warning'}
                      size="small"
                    />
                  </TableCell>
                  <TableCell>
                    <Chip
                      label={user.accountStatus}
                      color={user.accountStatus === 'ACTIVE' ? 'success' : 'default'}
                      size="small"
                    />
                  </TableCell>
                  <TableCell>
                    {!user.kycVerified && (
                      <Button
                        size="small"
                        startIcon={<CheckCircle />}
                        onClick={() => handleVerifyKyc(user.id)}
                      >
                        Verify KYC
                      </Button>
                    )}
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
}
