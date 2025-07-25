import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { Colors } from '@/constants/Colors';
import formatDate from '@/utils/formatDate';
import formatCurrency from '@/utils/formatCurrency';
import { Proposta } from '@/types/Proposta';
import { Link } from 'expo-router';

interface PropostaHistoricoProps
{
    item : Proposta;
    onPress? : () => void;
}

const PropostaHistoricoWidget : React.FC<PropostaHistoricoProps> = ({ item, onPress }) => {
    return (
        <Link href={{
                pathname: "/analise-proposta/[id]",
                params: { id : item.id }
            }as any} asChild>
            <TouchableOpacity>
                <View style={[styles.container, styles.card]}>
                    <View style={styles.infoContainer}>
                        <Text style={styles.widgetTitle}>Proposta</Text>
                        <Text style={styles.text}>Data da Solicitação: {formatDate(item.dataSolicitacao)}</Text>
                        <Text style={[styles.text, styles.valor]}>Valor da Proposta: {formatCurrency(item.valorProposta)}</Text>
                        <Text style={styles.text}>Parcelas solicitadas: {item.numeroParcelasSolicitadas}</Text>
                        {
                            item.numeroParcelasOfertadas ? (<Text style={styles.text}>Parcelas ofertadas: {item.numeroParcelasOfertadas}</Text>) : (<></>)
                        }
                        {
                            item.taxaJurosDiaria ? (<Text style={styles.text}>Taxa de Juros Diária: {item.taxaJurosDiaria}</Text>) : (<></>)
                        }

                    </View>
                </View>
            </TouchableOpacity>
        </Link>
    )
};

export default PropostaHistoricoWidget;

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
    card: 
    {
        backgroundColor: Colors.light.card,
        borderRadius: 16,
        padding: 20,
        marginBottom: 20,
        alignItems: 'center',
        shadowColor: "#000",
        shadowOffset: {
            width: 0,
            height: 2
        },
        shadowOpacity: 0.1,
        shadowRadius: 3.84,
        elevation: 5,
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