import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { Colors } from '@/constants/Colors';
import formatDate from '@/utils/formatDate';
import formatCurrency from '@/utils/formatCurrency';
import { PropostaHistorico } from '@/types/Proposta';

interface PropostaHistoricoItemProps
{
    item : PropostaHistorico;
    onPress? : () => void;
}

const PropostaHistoricoItemWidget : React.FC<PropostaHistoricoItemProps> = ({ item, onPress }) => {
    return (
        <TouchableOpacity onPress={onPress}>
            <View style={styles.container}>
                <View style={styles.infoContainer}>
                    <Text style={styles.widgetTitle}>Atualização</Text>
                    <Text style={styles.text}>Data da Atualização: {formatDate(item.dataAlteracao)}</Text>
                    <Text style={styles.text}>Última atualização feita por {item.atorAlteracao}.</Text>
                    {
                        item.statusAnterior ? (
                            <Text style={[styles.text]}>Status Anterior: {item.statusAnterior}</Text>
                        ) : (<></>)
                    }
                    {
                        item.statusNovo ? (
                            <Text style={[styles.text]}>Status Novo: {item.statusNovo}</Text>
                        ) : (<></>)
                    }
                    {
                        item.valorAnterior ? (
                            <Text style={[styles.text]}>Valor Anterior: {formatCurrency(item.valorAnterior)}</Text>
                        ) : (<></>)
                    }
                    {
                        item.valorNovo ? (
                            <Text style={[styles.text]}>Valor Novo: {formatCurrency(item.valorNovo)}</Text>
                        ) : (<></>)
                    }
                    {
                        item.numParcelasAnterior ? (
                            <Text style={[styles.text]}>Número de parcelas anterior: {item.numParcelasAnterior}</Text>
                        ) : (<></>)
                    }
                    {
                        item.numParcelasNovo ? (
                            <Text style={[styles.text]}>Número de parcelas novo: {item.numParcelasNovo}</Text>
                        ) : (<></>)
                    }
                    {
                        item.taxaJurosAnterior ? (
                            <Text style={[styles.text]}>Taxa de juros anterior: {item.taxaJurosAnterior}</Text>
                        ) : (<></>)
                    }
                    {
                        item.taxaJurosNova ? (
                            <Text style={[styles.text]}>Taxa de juros nova: {item.taxaJurosNova}</Text>
                        ) : (<></>)
                        
                    }
                    {
                        item.motivoRecusa ? (
                            <Text style={[styles.text]}>Motivo da Recusa: {item.motivoRecusa}</Text>
                        ) : (<></>)
                    }
                    {
                        item.observacoes ? (
                            <Text style={[styles.text]}>Obs: {item.observacoes}</Text>
                        ) : (<></>)
                    }
                </View>
            </View>
        </TouchableOpacity>
    )
};

export default PropostaHistoricoItemWidget;

const styles = StyleSheet.create(
{
    container: 
    {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        backgroundColor: Colors.light.card,
        padding: 15,
        borderRadius: 10,
        marginBottom: 10,
        borderWidth: 1,
        borderColor: Colors.light.border,
    },
    infoContainer: 
    {
        flex: 1,
    },
    widgetTitle: 
    {
        fontSize: 16,
        fontWeight: 'bold',
        color: Colors.light.text,
    },
    text: 
    {
        fontSize: 14,
        color: Colors.light.textSecondary,
        marginTop: 4,
    },
    valor: 
    {
        fontSize: 16,
        fontWeight: 'bold',
        color: Colors.light.primaryDark,
        marginTop: 4,
    },
    statusContainer: 
    {
        alignItems: 'flex-end',
    },
    pagarButton: 
    {
        backgroundColor: Colors.light.primary,
        paddingVertical: 8,
        paddingHorizontal: 20,
        borderRadius: 8,
        marginTop: 8,
    },
    pagarButtonText:
    {
        color: Colors.light.textOnPrimary,
        fontWeight: 'bold',
    },
    detalheValor:
    {
        fontSize: 12,
        color: Colors.light.textSecondary,
        fontStyle: 'italic',
        marginTop: 2,
    },
});