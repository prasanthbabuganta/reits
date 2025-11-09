import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Alert,
} from 'react-native';
import { transactionService } from '../../services/apiService';

export default function BuyScreen({ route, navigation }) {
  const { reit } = route.params;
  const [units, setUnits] = useState('');
  const [loading, setLoading] = useState(false);

  const totalAmount = parseFloat(units) * (reit.currentPrice || 0);
  const fees = totalAmount * 0.005;
  const totalCost = totalAmount + fees;

  const handleBuy = async () => {
    if (!units || parseFloat(units) <= 0) {
      Alert.alert('Error', 'Please enter a valid number of units');
      return;
    }

    setLoading(true);
    try {
      await transactionService.buy({
        reitId: reit.id,
        type: 'BUY',
        units: parseFloat(units),
        paymentMethod: 'WALLET',
      });

      Alert.alert(
        'Success',
        `Successfully purchased ${units} units of ${reit.ticker}`,
        [
          {
            text: 'OK',
            onPress: () => navigation.navigate('Portfolio'),
          },
        ]
      );
    } catch (error) {
      Alert.alert('Error', error.response?.data?.message || 'Purchase failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.reitInfo}>
        <Text style={styles.ticker}>{reit.ticker}</Text>
        <Text style={styles.price}>
          {reit.currency} {reit.currentPrice?.toFixed(2)} per unit
        </Text>
      </View>

      <View style={styles.form}>
        <Text style={styles.label}>Number of Units</Text>
        <TextInput
          style={styles.input}
          value={units}
          onChangeText={setUnits}
          keyboardType="decimal-pad"
          placeholder="0.00"
        />

        {units && parseFloat(units) > 0 && (
          <View style={styles.summary}>
            <View style={styles.summaryRow}>
              <Text style={styles.summaryLabel}>Subtotal</Text>
              <Text style={styles.summaryValue}>
                {reit.currency} {totalAmount.toFixed(2)}
              </Text>
            </View>
            <View style={styles.summaryRow}>
              <Text style={styles.summaryLabel}>Fees (0.5%)</Text>
              <Text style={styles.summaryValue}>
                {reit.currency} {fees.toFixed(2)}
              </Text>
            </View>
            <View style={[styles.summaryRow, styles.totalRow]}>
              <Text style={styles.totalLabel}>Total</Text>
              <Text style={styles.totalValue}>
                {reit.currency} {totalCost.toFixed(2)}
              </Text>
            </View>
          </View>
        )}

        <TouchableOpacity
          style={[styles.button, loading && styles.buttonDisabled]}
          onPress={handleBuy}
          disabled={loading}
        >
          <Text style={styles.buttonText}>
            {loading ? 'Processing...' : 'Confirm Purchase'}
          </Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
  reitInfo: {
    backgroundColor: '#1976d2',
    padding: 24,
    alignItems: 'center',
  },
  ticker: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#fff',
  },
  price: {
    fontSize: 16,
    color: '#fff',
    marginTop: 8,
  },
  form: {
    padding: 20,
  },
  label: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 8,
  },
  input: {
    borderWidth: 1,
    borderColor: '#ddd',
    padding: 16,
    borderRadius: 8,
    fontSize: 18,
  },
  summary: {
    marginTop: 24,
    padding: 16,
    backgroundColor: '#f5f5f5',
    borderRadius: 8,
  },
  summaryRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingVertical: 8,
  },
  summaryLabel: {
    fontSize: 14,
    color: '#666',
  },
  summaryValue: {
    fontSize: 14,
    fontWeight: '600',
  },
  totalRow: {
    borderTopWidth: 1,
    borderTopColor: '#ddd',
    marginTop: 8,
    paddingTop: 16,
  },
  totalLabel: {
    fontSize: 16,
    fontWeight: 'bold',
  },
  totalValue: {
    fontSize: 16,
    fontWeight: 'bold',
  },
  button: {
    backgroundColor: '#1976d2',
    padding: 16,
    borderRadius: 8,
    alignItems: 'center',
    marginTop: 24,
  },
  buttonDisabled: {
    opacity: 0.6,
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
});
