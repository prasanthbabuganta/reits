import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
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
import { Add } from '@mui/icons-material';
import { setREITs, setLoading } from '../../store/slices/reitsSlice';
import { reitService } from '../../services/apiService';

export default function REITList() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { reits, loading } = useSelector((state) => state.reits);

  useEffect(() => {
    loadREITs();
  }, []);

  const loadREITs = async () => {
    dispatch(setLoading(true));
    try {
      const response = await reitService.getAll();
      dispatch(setREITs(response.data));
    } catch (error) {
      console.error('Failed to load REITs:', error);
    }
  };

  return (
    <Box>
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          mb: 3,
        }}
      >
        <Typography variant="h4">REITs Management</Typography>
        <Button
          variant="contained"
          startIcon={<Add />}
          onClick={() => navigate('/reits/new')}
        >
          Add REIT
        </Button>
      </Box>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Ticker</TableCell>
              <TableCell>Name</TableCell>
              <TableCell>Sector</TableCell>
              <TableCell>Country</TableCell>
              <TableCell>Current Price</TableCell>
              <TableCell>Dividend Yield</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {loading ? (
              <TableRow>
                <TableCell colSpan={8} align="center">
                  Loading...
                </TableCell>
              </TableRow>
            ) : reits.length === 0 ? (
              <TableRow>
                <TableCell colSpan={8} align="center">
                  No REITs found
                </TableCell>
              </TableRow>
            ) : (
              reits.map((reit) => (
                <TableRow key={reit.id}>
                  <TableCell>{reit.ticker}</TableCell>
                  <TableCell>{reit.name}</TableCell>
                  <TableCell>{reit.sector}</TableCell>
                  <TableCell>{reit.country}</TableCell>
                  <TableCell>
                    {reit.currency} {reit.currentPrice?.toFixed(2) || 'N/A'}
                  </TableCell>
                  <TableCell>
                    {reit.dividendYield ? `${reit.dividendYield.toFixed(2)}%` : 'N/A'}
                  </TableCell>
                  <TableCell>
                    <Chip
                      label={reit.tradingEnabled ? 'Active' : 'Inactive'}
                      color={reit.tradingEnabled ? 'success' : 'default'}
                      size="small"
                    />
                  </TableCell>
                  <TableCell>
                    <Button
                      size="small"
                      onClick={() => navigate(`/reits/edit/${reit.id}`)}
                    >
                      Edit
                    </Button>
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
