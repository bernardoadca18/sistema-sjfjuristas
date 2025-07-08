import { Parcela } from "@/types/Emprestimo";
import formatCurrency from "@/utils/formatCurrency";
import formatDate from "@/utils/formatData";
import React from "react";
import { StyleSheet, Text, View } from "react-native";
import StatusWidget from "./StatusWidget";
import { Colors } from "@/constants/Colors";

interface PagamentoWidgetProps {
    pagamento: Parcela;
}

const PagamentoWidget : React.FC<PagamentoWidgetProps> = ( { pagamento } ) => {
    return (
        <View style={styles.row}>
            <Text style={styles.text}>{formatDate(pagamento.dataVencimento)}</Text>
            <Text style={[styles.text, styles.value]}>{formatCurrency(pagamento.valorTotalParcela)}</Text>
            <StatusWidget status={pagamento.statusPagamentoParcelaNome}></StatusWidget>
        </View>
    )
}

const styles = StyleSheet.create({
    row: {
        
        flexDirection: 'row',       
        justifyContent: 'space-between', 
        alignItems: 'center',       
        
        width: '100%',
        paddingVertical: 12,
        borderBottomWidth: 1,
        borderBottomColor: '#f0f0f0',
    },
    text: {
        fontSize: 16,
        color: Colors.light.textSecondary,
    },
    value: {
        color: Colors.light.text,
        fontWeight: 'bold',
    }
})

export default PagamentoWidget;