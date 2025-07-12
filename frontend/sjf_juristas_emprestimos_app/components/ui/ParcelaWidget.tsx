import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { Link } from 'expo-router';
import { Parcela } from '@/types/Emprestimo';
import { Colors } from '@/constants/Colors';
import formatDate from '@/utils/formatDate';
import StatusWidget from './StatusWidget';
import formatCurrency from '@/utils/formatCurrency';

interface ParcelaWidgetProps
{
    parcela: Parcela;
    isNextPayment: boolean;
    onPress?: () => void;
}

const ParcelaWidget: React.FC<ParcelaWidgetProps> = ( { parcela, isNextPayment, onPress } ) => {
    const temDetalhes = parcela.valorPrincipal > 0 || parcela.valorJuros > 0;

    return (
        <TouchableOpacity onPress={onPress} disabled={!onPress}>
            <View style={styles.container}>
                <View style={styles.infoContainer}>
                    <Text style={styles.parcelaNumero}>Parcela {parcela.numeroParcela}</Text>
                    <Text style={styles.text}>Vencimento: {formatDate(parcela.dataVencimento)}</Text>
                    <Text style={[styles.text, styles.valor]}>{formatCurrency(parcela.valorTotalParcela)}</Text>
                    {
                        temDetalhes && (
                            <Text style={styles.detalheValor}>
                                ({formatCurrency(parcela.valorPrincipal)} de principal + {formatCurrency(parcela.valorJuros)} de juros)
                            </Text>
                        )
                    }
                </View>

                <View style={styles.statusContainer}>
                    <StatusWidget status={parcela.statusPagamentoParcelaNome}/>
                        {
                            isNextPayment && (
                                <Link href={{ pathname: "/payment/[id]", params: {id: parcela.id} }} asChild>
                                    <TouchableOpacity style={styles.pagarButton}>
                                        <Text style={styles.pagarButtonText}>
                                            Pagar
                                        </Text>
                                    </TouchableOpacity>
                                </Link>
                            )
                        }
                </View>
            </View>
        </TouchableOpacity>
    )
}

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
    parcelaNumero: 
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

export default ParcelaWidget;