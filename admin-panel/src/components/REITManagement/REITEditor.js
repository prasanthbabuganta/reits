import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import {
  Box,
  Button,
  Paper,
  TextField,
  Typography,
  Grid,
  MenuItem,
  Switch,
  FormControlLabel,
} from '@mui/material';
import { reitService } from '../../services/apiService';

const sectors = [
  'RETAIL',
  'OFFICE',
  'INDUSTRIAL',
  'HOSPITALITY',
  'HEALTHCARE',
  'DATA_CENTER',
  'RESIDENTIAL',
  'DIVERSIFIED',
  'SPECIALTY',
];

export default function REITEditor() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    ticker: '',
    name: '',
    exchange: '',
    sector: 'RETAIL',
    country: 'Singapore',
    currency: 'SGD',
    currentPrice: '',
    dividendYield: '',
    priceToBook: '',
    occupancyRate: '',
    marketCap: '',
    description: '',
    tradingEnabled: true,
    isActive: true,
  });

  useEffect(() => {
    if (id) {
      loadREIT();
    }
  }, [id]);

  const loadREIT = async () => {
    try {
      const response = await reitService.getById(id);
      setFormData(response.data);
    } catch (error) {
      console.error('Failed to load REIT:', error);
    }
  };

  const handleChange = (e) => {
    const { name, value, checked, type } = e.target;
    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (id) {
        await reitService.update(id, formData);
      } else {
        await reitService.create(formData);
      }
      navigate('/reits');
    } catch (error) {
      console.error('Failed to save REIT:', error);
    }
  };

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        {id ? 'Edit REIT' : 'Add New REIT'}
      </Typography>

      <Paper sx={{ p: 3 }}>
        <form onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                required
                label="Ticker"
                name="ticker"
                value={formData.ticker}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                required
                label="Name"
                name="name"
                value={formData.name}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                required
                label="Exchange"
                name="exchange"
                value={formData.exchange}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                required
                select
                label="Sector"
                name="sector"
                value={formData.sector}
                onChange={handleChange}
              >
                {sectors.map((sector) => (
                  <MenuItem key={sector} value={sector}>
                    {sector}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                required
                label="Country"
                name="country"
                value={formData.country}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                required
                label="Currency"
                name="currency"
                value={formData.currency}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                type="number"
                label="Current Price"
                name="currentPrice"
                value={formData.currentPrice}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                type="number"
                label="Dividend Yield (%)"
                name="dividendYield"
                value={formData.dividendYield}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                multiline
                rows={4}
                label="Description"
                name="description"
                value={formData.description}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <FormControlLabel
                control={
                  <Switch
                    checked={formData.tradingEnabled}
                    onChange={handleChange}
                    name="tradingEnabled"
                  />
                }
                label="Trading Enabled"
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <FormControlLabel
                control={
                  <Switch
                    checked={formData.isActive}
                    onChange={handleChange}
                    name="isActive"
                  />
                }
                label="Active"
              />
            </Grid>
            <Grid item xs={12}>
              <Box sx={{ display: 'flex', gap: 2 }}>
                <Button type="submit" variant="contained">
                  {id ? 'Update' : 'Create'}
                </Button>
                <Button variant="outlined" onClick={() => navigate('/reits')}>
                  Cancel
                </Button>
              </Box>
            </Grid>
          </Grid>
        </form>
      </Paper>
    </Box>
  );
}
