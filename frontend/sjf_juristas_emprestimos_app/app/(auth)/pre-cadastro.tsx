import { useRouter } from 'expo-router';
import React, { useState } from 'react';
import { Alert, Button, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { useAuth } from '../../context/AuthContext';
import Masks from '@/utils/masks';
import MaskInput from 'react-native-mask-input';
import { Colors } from '@/constants/Colors';

const PreCadastroScreen : React.FC = () => {
    const [cpf, setCpf] = useState('');
    const [dataNascimento, setDataNascimento] = useState('');
    
    const [unmaskedCpf, setUnmaskedCpf] = useState('');
    const [unmaskedDataNascimento, setUnmaskedDataNascimento] = useState('');
    
    const [email, setEmail] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const { checkPreCadastro } = useAuth();

    const router = useRouter();

    const cpfMask = Masks.cpfMask;
    const dateMask = Masks.dateMask;

    const handleVerify = async () => {
        const formattedDate = unmaskedDataNascimento.length === 8 ? `${unmaskedDataNascimento.substring(4, 8)}-${unmaskedDataNascimento.substring(2, 4)}-${unmaskedDataNascimento.substring(0, 2)}`: '';

        if (!unmaskedCpf && (!email || !dataNascimento))
        {
            Alert.alert('Dados Incompletos', 'Por favor, informe o CPF ou o E-mail e a Data de Nascimento.');
            return;
        }

        setIsLoading(true);
        const result = await checkPreCadastro({ cpf: unmaskedCpf, email: email, dataNascimento: formattedDate });
        setIsLoading(false);

        if (result.success)
        {
            router.push('/(auth)/finalizar-cadastro');
        }
        else
        {
            Alert.alert('Verificação Falhou', result.error);
        }
    };

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Primeiro Acesso</Text>
            <Text style={styles.subtitle}>Informe os dados utilizados na sua proposta para continuar.</Text>

            <MaskInput style={styles.input} value={cpf} mask={cpfMask} placeholder='CPF' keyboardType='numeric' onChangeText={(masked, unmasked) => { setCpf(masked); setUnmaskedCpf(unmasked) }}></MaskInput>
            <TextInput style={styles.input} placeholder="E-mail" value={email} onChangeText={setEmail} keyboardType="email-address" autoCapitalize="none" />
            <MaskInput style={styles.input} value={dataNascimento} mask={dateMask} placeholder='Data de Nascimento (DD/MM/AAAA)' keyboardType='numeric' onChangeText={(masked, unmasked) => { setDataNascimento(masked); setUnmaskedDataNascimento(unmasked) }}></MaskInput>


            <TouchableOpacity onPress={handleVerify} disabled={isLoading} style={[styles.button, isLoading && styles.buttonDisabled]}>
                <Text style={styles.buttonText}>
                    {isLoading ? "Verificando..." : "Continuar"}
                </Text>
            </TouchableOpacity>

            <TouchableOpacity onPress={() => router.back()} style={[styles.backButton, isLoading && styles.buttonDisabled]}>
                <Text style={styles.buttonText}>
                    {"Voltar para o Login"}
                </Text>
            </TouchableOpacity>
        </View>
    );
}

const styles = StyleSheet.create({
    container: { flex: 1, justifyContent: 'center', padding: 20, backgroundColor: Colors.light.background },
    title: { fontSize: 28, fontWeight: 'bold', textAlign: 'center', marginBottom: 10, color: Colors.light.primaryDark },
    subtitle: { fontSize: 16, textAlign: 'center', color: Colors.light.textSecondary, marginBottom: 30 },
    input: { height: 50, borderColor: Colors.light.border, borderWidth: 1, borderRadius: 8, paddingHorizontal: 15, marginBottom: 15, backgroundColor: Colors.light.card, color: Colors.light.text, fontSize: 16, },
    button: { backgroundColor: Colors.light.primary, paddingVertical: 15, borderRadius: 8, alignItems: 'center', },
    buttonDisabled: { backgroundColor: Colors.light.primaryLight },
    buttonText: { color: Colors.light.textOnPrimary, fontSize: 16, fontWeight: 'bold', },
    backButton: { marginTop: 15, backgroundColor: Colors.light.backButton, paddingVertical: 15, borderRadius: 8, alignItems: 'center' },
    backButtonText: { color: Colors.light.textSecondary, textAlign: 'center', fontSize: 16, }
});

export default PreCadastroScreen;