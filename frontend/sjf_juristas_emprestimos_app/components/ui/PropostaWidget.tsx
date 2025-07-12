import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { Proposta } from '@/types/Proposta';
import formatCurrency from '@/utils/formatCurrency';
import formatDate from '@/utils/formatDate';
import StatusWidget from './StatusWidget';

export interface PropostaWidgetProps {
  proposta: Proposta;
  onPress?: () => void;
}

const PropostaWidget: React.FC<PropostaWidgetProps> = ({ proposta, onPress }) => {
  return (
    <TouchableOpacity onPress={onPress} disabled={!onPress} style={styles.card}>
      <View style={styles.header}>
        <Text style={styles.title}>Proposta de Empréstimo</Text>
        <StatusWidget status={proposta.statusProposta} />
      </View>
      <View style={styles.detailsContainer}>
        <View style={styles.detailItem}>
          <Text style={styles.label}>Valor Solicitado</Text>
          <Text style={styles.value}>{formatCurrency(proposta.valorSolicitado)}</Text>
        </View>
        <View style={styles.detailItem}>
          <Text style={styles.label}>Data da Solicitação</Text>
          <Text style={styles.value}>{formatDate(proposta.dataSolicitacao)}</Text>
        </View>
      </View>
      {proposta.valorOfertado && (
         <View style={styles.detailItem}>
            <Text style={styles.label}>Última Oferta</Text>
            <Text style={styles.value}>{formatCurrency(proposta.valorOfertado)}</Text>
        </View>
      )}
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  card: {
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 16,
    marginVertical: 8,
    marginHorizontal: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.2,
    shadowRadius: 2,
    elevation: 3,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#eee',
    paddingBottom: 12,
  },
  title: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#333',
  },
  detailsContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 8,
  },
  detailItem: {
    alignItems: 'flex-start',
  },
  label: {
    fontSize: 12,
    color: '#888',
  },
  value: {
    fontSize: 16,
    fontWeight: '500',
    color: '#333',
  },
});

export default PropostaWidget;