import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { Stack, useLocalSearchParams } from 'expo-router';

const PaymentScreen = () => {
  const { id } = useLocalSearchParams<{ id: string }>();

  return (
    <View style={styles.container}>
      <Stack.Screen
        options={{
          title: 'Pagamento',
          headerBackTitle: 'Voltar',
        }}
      />
      <View style={styles.content}>
        <Text style={styles.title}>Tela de Pagamento</Text>
        <Text style={styles.subtitle}>ID da Parcela: {id}</Text>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: '#fff',
  },
  content: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  title: {
    fontSize: 22,
    fontWeight: 'bold',
  },
  subtitle: {
    fontSize: 16,
    marginTop: 10,
  },
});

// Esta linha resolve o aviso
export default PaymentScreen;