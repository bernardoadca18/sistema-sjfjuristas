import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { PropostaHistorico } from '@/types/Proposta';
import { Colors } from '@/constants/Colors';
import formatDate from '@/utils/formatDate';
import formatCurrency from '@/utils/formatCurrency';
import { Ionicons } from '@expo/vector-icons';

interface PropostaHistoricoWidgetProps
{
    item: PropostaHistorico;
}

const getIconForActor = (actor: string) => {
    return actor === 'CLIENTE' ? 'person-circle-outline' : 'business-outline';
}

const formatNumber = (num?: number) => (num != null ? num : '-');
const formatMoney = (val?: number) => (val != null ? formatCurrency(val) : '-');

const PropostaHistoricoWidget : React.FC<PropostaHistoricoWidgetProps> = ({ item }) => {
    return (
    <View style={styles.container}>
        <View style={styles.header}>
        <Ionicons name={getIconForActor(item.atorAlteracao)} size={24} color={Colors.light.primary} />
        <Text style={styles.actorText}>{item.atorAlteracao === 'CLIENTE' ? 'Você' : 'SJF Juristas'}</Text>
        <Text style={styles.dateText}>{formatDate(item.dataAlteracao)}</Text>
      </View>
      <View style={styles.content}>
        {item.observacoes && <Text style={styles.observationText}>{item.observacoes}</Text>}
        
        <View style={styles.changeGrid}>
            { (item.statusNovo || item.statusAnterior) && 
                <View style={styles.changeItem}>
                    <Text style={styles.label}>Status</Text>
                    <Text style={styles.value}>{item.statusAnterior || '-'} → {item.statusNovo || '-'}</Text>
                </View>
            }
            { (item.valorNovo || item.valorAnterior) && 
                <View style={styles.changeItem}>
                    <Text style={styles.label}>Valor</Text>
                    <Text style={styles.value}>{formatMoney(item.valorAnterior)} → {formatMoney(item.valorNovo)}</Text>
                </View>
            }
             { (item.numParcelasNovo || item.numParcelasAnterior) && 
                <View style={styles.changeItem}>
                    <Text style={styles.label}>Parcelas</Text>
                    <Text style={styles.value}>{formatNumber(item.numParcelasAnterior)} → {formatNumber(item.numParcelasNovo)}</Text>
                </View>
            }
             { (item.taxaJurosNova || item.taxaJurosAnterior) && 
                <View style={styles.changeItem}>
                    <Text style={styles.label}>Taxa de Juros</Text>
                    <Text style={styles.value}>{item.taxaJurosAnterior || '-'}% → {item.taxaJurosNova || '-'}%</Text>
                </View>
            }
        </View>

        {item.motivoRecusa && <Text style={styles.reasonText}>Motivo da Recusa: {item.motivoRecusa}</Text>}

      </View>
    </View>
    );
};


const styles = StyleSheet.create({
    container:
    {
        backgroundColor: '#fff',
        borderRadius: 12,
        marginVertical: 8,
        padding: 16,
        elevation: 2,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 1 },
        shadowOpacity: 0.1,
        shadowRadius: 2,
    },
    header:
    {
        flexDirection: 'row',
        alignItems: 'center',
        marginBottom: 12,
        borderBottomWidth: 1,
        borderBottomColor: '#f0f0f0',
        paddingBottom: 8,
    },
    actorText:
    {
        fontWeight: 'bold',
        fontSize: 16,
        color: Colors.light.text,
        marginLeft: 8,
        flex: 1,
    },
    dateText:
    {
        fontSize: 12,
        color: Colors.light.textSecondary,
    },
    content:{},
    observationText:
    {
        fontSize: 14,
        fontStyle: 'italic',
        color: '#555',
        marginBottom: 12,
    },
    changeGrid: {},
    changeItem:
    {
        flexDirection: 'row',
        justifyContent: 'space-between',
        paddingVertical: 4,
    },
    label:
    {
        fontSize: 14,
        color: Colors.light.textSecondary,
        fontWeight: '500',
    },
    value:
    {
        fontSize: 14,
        color: Colors.light.text,
        fontWeight: 'bold',
    },
    reasonText:
    {
        marginTop: 12,
        fontSize: 14,
        color: Colors.light.atrasadoText,
        fontWeight: 'bold',
        paddingTop: 8,
        borderTopWidth: 1,
        borderTopColor: '#f0f0f0',
    }
});

export default PropostaHistoricoWidget;