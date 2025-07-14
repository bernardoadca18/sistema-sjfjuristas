import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { PropostaHistorico } from '@/types/Proposta';
import { Colors } from '@/constants/Colors';
import formatDate from '@/utils/formatDate';
import formatCurrency from '@/utils/formatCurrency';
import { Ionicons } from '@expo/vector-icons';
import { Emprestimo } from '@/types/Emprestimo';

interface EmprestimoHistoricoWidgetProps
{
    item : Emprestimo;
}

const EmprestimoHistoricoWidget : React.FC<EmprestimoHistoricoWidgetProps> = ({ item }) => {
    return (
        <View>
            <Text>Empréstimo</Text>
            <Text>{ item.id }</Text>
        </View>
    )
};