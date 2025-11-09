import React, { useEffect } from 'react';
import {
  View,
  Text,
  FlatList,
  TouchableOpacity,
  StyleSheet,
  ActivityIndicator,
} from 'react-native';
import { useDispatch, useSelector } from 'react-redux';
import { setREITs, setLoading } from '../../store/slices/reitsSlice';
import { reitService } from '../../services/apiService';

export default function MarketOverviewScreen({ navigation }) {
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

  const renderREIT = ({ item }) => (
    <TouchableOpacity
      style={styles.reitCard}
      onPress={() => navigation.navigate('REITDetails', { reit: item })}
    >
      <View style={styles.reitHeader}>
        <View>
          <Text style={styles.reitTicker}>{item.ticker}</Text>
          <Text style={styles.reitName}>{item.name}</Text>
        </View>
        <View style={styles.reitPriceContainer}>
          <Text style={styles.reitPrice}>
            {item.currency} {item.currentPrice?.toFixed(2)}
          </Text>
          <Text style={styles.reitYield}>
            Yield: {item.dividendYield?.toFixed(2)}%
          </Text>
        </View>
      </View>
      <View style={styles.reitFooter}>
        <Text style={styles.reitSector}>{item.sector}</Text>
        <Text style={styles.reitCountry}>{item.country}</Text>
      </View>
    </TouchableOpacity>
  );

  if (loading) {
    return (
      <View style={styles.centerContainer}>
        <ActivityIndicator size="large" color="#1976d2" />
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <FlatList
        data={reits}
        renderItem={renderREIT}
        keyExtractor={(item) => item.id.toString()}
        contentContainerStyle={styles.list}
        ListEmptyComponent={
          <Text style={styles.emptyText}>No REITs available</Text>
        }
      />
    </View>
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
  list: {
    padding: 16,
  },
  reitCard: {
    backgroundColor: '#fff',
    borderRadius: 12,
    padding: 16,
    marginBottom: 12,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
  },
  reitHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 12,
  },
  reitTicker: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#1976d2',
  },
  reitName: {
    fontSize: 14,
    color: '#666',
    marginTop: 4,
  },
  reitPriceContainer: {
    alignItems: 'flex-end',
  },
  reitPrice: {
    fontSize: 18,
    fontWeight: 'bold',
  },
  reitYield: {
    fontSize: 14,
    color: '#2e7d32',
    marginTop: 4,
  },
  reitFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  reitSector: {
    fontSize: 12,
    color: '#666',
  },
  reitCountry: {
    fontSize: 12,
    color: '#666',
  },
  emptyText: {
    textAlign: 'center',
    marginTop: 50,
    fontSize: 16,
    color: '#999',
  },
});
