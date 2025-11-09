import React, { useEffect, useState } from 'react';
import {
  Grid,
  Paper,
  Typography,
  Box,
  Card,
  CardContent,
} from '@mui/material';
import {
  People,
  BusinessCenter,
  TrendingUp,
  AttachMoney,
} from '@mui/icons-material';
import { adminService } from '../../services/apiService';

export default function Dashboard() {
  const [stats, setStats] = useState({
    totalUsers: 0,
    totalREITs: 0,
    totalTransactions: 0,
    newUsersLast30Days: 0,
  });

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      const response = await adminService.getStats();
      setStats(response.data);
    } catch (error) {
      console.error('Failed to load stats:', error);
    }
  };

  const statCards = [
    {
      title: 'Total Users',
      value: stats.totalUsers,
      icon: <People fontSize="large" />,
      color: '#1976d2',
    },
    {
      title: 'Total REITs',
      value: stats.totalREITs,
      icon: <BusinessCenter fontSize="large" />,
      color: '#2e7d32',
    },
    {
      title: 'Total Transactions',
      value: stats.totalTransactions,
      icon: <TrendingUp fontSize="large" />,
      color: '#ed6c02',
    },
    {
      title: 'New Users (30d)',
      value: stats.newUsersLast30Days,
      icon: <AttachMoney fontSize="large" />,
      color: '#9c27b0',
    },
  ];

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Dashboard
      </Typography>

      <Grid container spacing={3}>
        {statCards.map((card, index) => (
          <Grid item xs={12} sm={6} md={3} key={index}>
            <Card>
              <CardContent>
                <Box
                  sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                  }}
                >
                  <Box>
                    <Typography color="textSecondary" variant="body2">
                      {card.title}
                    </Typography>
                    <Typography variant="h4" sx={{ mt: 1 }}>
                      {card.value.toLocaleString()}
                    </Typography>
                  </Box>
                  <Box sx={{ color: card.color }}>{card.icon}</Box>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      <Grid container spacing={3} sx={{ mt: 2 }}>
        <Grid item xs={12}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Platform Overview
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Welcome to the REITs Investment Platform Admin Panel. Use the navigation
              menu to manage REITs, users, and view detailed analytics.
            </Typography>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
}
