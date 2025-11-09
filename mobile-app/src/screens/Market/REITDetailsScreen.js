import React from 'react';
import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
} from 'react-native';

export default function REITDetailsScreen({ route, navigation }) {
  const { reit } = route.params;

  const DetailRow = ({ label, value }) => (
    <View style={styles.detailRow}>
      <Text style={styles.detailLabel}>{label}</Text>
      <Text style={styles.detailValue}>{value}</Text>
    </View>
  );

  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.ticker}>{reit.ticker}</Text>
        <Text style={styles.name}>{reit.name}</Text>
        <Text style={styles.price}>
          {reit.currency} {reit.currentPrice?.toFixed(2)}
        </Text>
      </View>

      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Key Metrics</Text>
        <DetailRow label="Dividend Yield" value={`${reit.dividendYield?.toFixed(2)}%`} />
        <DetailRow label="Price to Book" value={reit.priceToBook?.toFixed(2)} />
        <DetailRow label="Occupancy Rate" value={`${reit.occupancyRate?.toFixed(2)}%`} />
        <DetailRow label="Market Cap" value={`${reit.currency} ${(reit.marketCap / 1000000).toFixed(2)}M`} />
      </View>

      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Information</Text>
        <DetailRow label="Sector" value={reit.sector} />
        <DetailRow label="Country" value={reit.country} />
        <DetailRow label="Exchange" value={reit.exchange} />
      </View>

      {reit.description && (
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Description</Text>
          <Text style={styles.description}>{reit.description}</Text>
        </View>
      )}

      <TouchableOpacity
        style={styles.buyButton}
        onPress={() => navigation.navigate('Buy', { reit })}
      >
        <Text style={styles.buyButtonText}>Buy Now</Text>
      </TouchableOpacity>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
  header: {
    padding: 20,
    backgroundColor: '#1976d2',
    alignItems: 'center',
  },
  ticker: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#fff',
  },
  name: {
    fontSize: 16,
    color: '#fff',
    marginTop: 8,
  },
  price: {
    fontSize: 32,
    fontWeight: 'bold',
    color: '#fff',
    marginTop: 16,
  },
  section: {
    padding: 20,
    borderBottomWidth: 1,
    borderBottomColor: '#eee',
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 16,
  },
  detailRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingVertical: 12,
  },
  detailLabel: {
    fontSize: 14,
    color: '#666',
  },
  detailValue: {
    fontSize: 14,
    fontWeight: '600',
  },
  description: {
    fontSize: 14,
    color: '#666',
    lineHeight: 20,
  },
  buyButton: {
    backgroundColor: '#1976d2',
    margin: 20,
    padding: 16,
    borderRadius: 8,
    alignItems: 'center',
  },
  buyButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
});
