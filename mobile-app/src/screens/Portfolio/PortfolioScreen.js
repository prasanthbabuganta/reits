import React, { useEffect } from 'react';
import {
  View,
  Text,
  ScrollView,
  StyleSheet,
  ActivityIndicator,
  RefreshControl,
} from 'react-native';
import { useDispatch, useSelector } from 'react-redux';
import { setPortfolio, setLoading } from '../../store/slices/portfolioSlice';
import { portfolioService } from '../../services/apiService';

export default function PortfolioScreen() {
  const dispatch = useDispatch();
  const { portfolio, holdings, loading } = useSelector((state) => state.portfolio);
  const [refreshing, setRefreshing] = React.useState(false);

  useEffect(() => {
    loadPortfolio();
  }, []);

  const loadPortfolio = async () => {
    dispatch(setLoading(true));
    try {
      const response = await portfolioService.getPortfolio();
      dispatch(setPortfolio(response.data));
    } catch (error) {
      console.error('Failed to load portfolio:', error);
    }
  };

  const onRefresh = async () => {
    setRefreshing(true);
    await loadPortfolio();
    setRefreshing(false);
  };

  if (loading && !portfolio) {
    return (
      <View style={styles.centerContainer}>
        <ActivityIndicator size="large" color="#1976d2" />
      </View>
    );
  }

  const gain = portfolio?.unrealizedGain || 0;
  const gainPercentage = portfolio?.unrealizedGainPercentage || 0;
  const isProfit = gain >= 0;

  return (
    <ScrollView
      style={styles.container}
      refreshControl={
        <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
      }
    >
      <View style={styles.summaryCard}>
        <Text style={styles.summaryLabel}>Total Portfolio Value</Text>
        <Text style={styles.summaryValue}>
          ${portfolio?.totalValue?.toFixed(2) || '0.00'}
        </Text>
        <View style={styles.gainContainer}>
          <Text style={[styles.gain, isProfit ? styles.profit : styles.loss]}>
            {isProfit ? '+' : ''}{gain.toFixed(2)} ({gainPercentage.toFixed(2)}%)
          </Text>
        </View>
      </View>

      <View style={styles.metricsContainer}>
        <View style={styles.metricCard}>
          <Text style={styles.metricLabel}>Invested</Text>
          <Text style={styles.metricValue}>
            ${portfolio?.totalInvested?.toFixed(2) || '0.00'}
          </Text>
        </View>
        <View style={styles.metricCard}>
          <Text style={styles.metricLabel}>Annual Dividends</Text>
          <Text style={styles.metricValue}>
            ${portfolio?.annualDividendIncome?.toFixed(2) || '0.00'}
          </Text>
        </View>
      </View>

      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Holdings</Text>
        {holdings.length === 0 ? (
          <Text style={styles.emptyText}>No holdings yet</Text>
        ) : (
          holdings.map((holding) => (
            <View key={holding.holdingId} style={styles.holdingCard}>
              <View style={styles.holdingHeader}>
                <View>
                  <Text style={styles.holdingTicker}>{holding.reitTicker}</Text>
                  <Text style={styles.holdingName}>{holding.reitName}</Text>
                </View>
                <View style={styles.holdingValueContainer}>
                  <Text style={styles.holdingValue}>
                    ${holding.currentValue?.toFixed(2)}
                  </Text>
                  <Text
                    style={[
                      styles.holdingGain,
                      holding.unrealizedGain >= 0 ? styles.profit : styles.loss,
                    ]}
                  >
                    {holding.unrealizedGain >= 0 ? '+' : ''}
                    {holding.unrealizedGain?.toFixed(2)} (
                    {holding.unrealizedGainPercentage?.toFixed(2)}%)
                  </Text>
                </View>
              </View>
              <View style={styles.holdingDetails}>
                <Text style={styles.holdingDetail}>
                  {holding.totalUnits?.toFixed(4)} units @ $
                  {holding.averageCost?.toFixed(2)}
                </Text>
              </View>
            </View>
          ))
        )}
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  centerContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  summaryCard: {
    backgroundColor: '#1976d2',
    padding: 24,
    margin: 16,
    borderRadius: 12,
    alignItems: 'center',
  },
  summaryLabel: {
    fontSize: 14,
    color: '#fff',
    opacity: 0.9,
  },
  summaryValue: {
    fontSize: 36,
    fontWeight: 'bold',
    color: '#fff',
    marginTop: 8,
  },
  gainContainer: {
    marginTop: 8,
  },
  gain: {
    fontSize: 16,
    fontWeight: '600',
  },
  profit: {
    color: '#4caf50',
  },
  loss: {
    color: '#f44336',
  },
  metricsContainer: {
    flexDirection: 'row',
    paddingHorizontal: 16,
    gap: 12,
  },
  metricCard: {
    flex: 1,
    backgroundColor: '#fff',
    padding: 16,
    borderRadius: 8,
    elevation: 2,
  },
  metricLabel: {
    fontSize: 12,
    color: '#666',
  },
  metricValue: {
    fontSize: 18,
    fontWeight: 'bold',
    marginTop: 4,
  },
  section: {
    margin: 16,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 12,
  },
  holdingCard: {
    backgroundColor: '#fff',
    padding: 16,
    borderRadius: 8,
    marginBottom: 12,
    elevation: 2,
  },
  holdingHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 8,
  },
  holdingTicker: {
    fontSize: 16,
    fontWeight: 'bold',
  },
  holdingName: {
    fontSize: 12,
    color: '#666',
    marginTop: 4,
  },
  holdingValueContainer: {
    alignItems: 'flex-end',
  },
  holdingValue: {
    fontSize: 16,
    fontWeight: 'bold',
  },
  holdingGain: {
    fontSize: 12,
    marginTop: 4,
  },
  holdingDetails: {
    marginTop: 8,
  },
  holdingDetail: {
    fontSize: 12,
    color: '#666',
  },
  emptyText: {
    textAlign: 'center',
    color: '#999',
    marginTop: 20,
  },
});
