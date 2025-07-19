import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ActivityIndicator, Alert, TextInput } from 'react-native';
import { useLocalSearchParams, useRouter } from 'expo-router';
import { getPropostaById, responderProposta } from '@/services/propostaService';
import { Proposta } from '@/types/Proposta';
import { Colors } from '@/constants/Colors';
import formatCurrency from '@/utils/formatCurrency';
import formatDate from '@/utils/formatDate';

const AnalisePropostaScreen = () => {
    const { id } = useLocalSearchParams<{ id: string }>();
    const router = useRouter();
    const [proposta, setProposta] = useState<Proposta | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [motivoRecusa, setMotivoRecusa] = useState('');

    useEffect(() => {
        if (id) {
            getPropostaById(id)
                .then(setProposta)
                .catch(err => {
                    console.error(err);
                    Alert.alert("Erro", "Não foi possível carregar os detalhes da proposta.");
                })
                .finally(() => setIsLoading(false));
        }
    }, [id]);

    const handleAction = async (acao: 'ACEITAR' | 'RECUSAR') => {
        if (!id) return;

        if (acao === 'RECUSAR' && !motivoRecusa) {
            Alert.alert("Atenção", "Por favor, informe o motivo da recusa.");
            return;
        }

        try {
            await responderProposta(id, acao, motivoRecusa);
            Alert.alert("Sucesso", `Proposta ${acao === 'ACEITAR' ? 'aceita' : 'recusada'} com sucesso!`);
            router.back();
        } catch (error) {
            console.error(error);
            Alert.alert("Erro", `Não foi possível ${acao === 'ACEITAR' ? 'aceitar' : 'recusar'} a proposta.`);
        }
    };

    if (isLoading) {
        return <ActivityIndicator size="large" color={Colors.light.primary} style={styles.loader} />;
    }

    if (!proposta) {
        return <Text style={styles.errorText}>Proposta não encontrada.</Text>;
    }

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Análise de Contraproposta</Text>

            <View style={styles.card}>
                <Text style={styles.label}>Valor Ofertado:</Text>
                <Text style={styles.value}>{formatCurrency(proposta.valorProposta)}</Text>

                <Text style={styles.label}>Número de Parcelas:</Text>
                <Text style={styles.value}>{proposta.numeroParcelasOfertadas}</Text>

                <Text style={styles.label}>Taxa de Juros Diária:</Text>
                <Text style={styles.value}>{proposta.taxaJurosDiaria}%</Text>
                
                <Text style={styles.label}>Data da Proposta:</Text>
                <Text style={styles.value}>{formatDate(proposta.dataSolicitacao)}</Text>
            </View>

            {proposta.statusProposta === 'AGUARDANDO_CLIENTE' && (
                <>
                    <TextInput
                        style={styles.input}
                        placeholder="Motivo da Recusa (opcional se aceitar)"
                        value={motivoRecusa}
                        onChangeText={setMotivoRecusa}
                        multiline
                    />
                    <View style={styles.buttonContainer}>
                        <TouchableOpacity style={[styles.button, styles.acceptButton]} onPress={() => handleAction('ACEITAR')}>
                            <Text style={styles.buttonText}>Aceitar</Text>
                        </TouchableOpacity>
                        <TouchableOpacity style={[styles.button, styles.rejectButton]} onPress={() => handleAction('RECUSAR')}>
                            <Text style={styles.buttonText}>Recusar</Text>
                        </TouchableOpacity>
                    </View>
                </>
            )}
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 20,
        backgroundColor: Colors.light.background,
    },
    loader: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    errorText: {
        textAlign: 'center',
        marginTop: 20,
        fontSize: 16,
        color: 'red',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        marginBottom: 20,
        textAlign: 'center',
        color: Colors.light.primaryDark,
    },
    card: {
        backgroundColor: Colors.light.card,
        borderRadius: 10,
        padding: 20,
        marginBottom: 20,
    },
    label: {
        fontSize: 16,
        fontWeight: 'bold',
        color: Colors.light.text,
        marginBottom: 5,
    },
    value: {
        fontSize: 16,
        color: Colors.light.textSecondary,
        marginBottom: 15,
    },
    input: {
        borderWidth: 1,
        borderColor: Colors.light.border,
        borderRadius: 8,
        padding: 10,
        marginBottom: 20,
        minHeight: 100,
        textAlignVertical: 'top',
    },
    buttonContainer: {
        flexDirection: 'row',
        justifyContent: 'space-around',
    },
    button: {
        paddingVertical: 12,
        paddingHorizontal: 30,
        borderRadius: 8,
        alignItems: 'center',
        justifyContent: 'center',
    },
    acceptButton: {
        backgroundColor: Colors.light.primary,
    },
    rejectButton: {
        backgroundColor: Colors.light.atrasadoText,
    },
    buttonText: {
        color: '#fff',
        fontWeight: 'bold',
        fontSize: 16,
    },
});

export default AnalisePropostaScreen;
