import { useRouter } from 'expo-router';
import React, { useState } from 'react';
import { Alert, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { Colors } from '@/constants/Colors';
import { solicitarRedefinicaoSenha } from '@/services/senhaService';


const SolicitarRedefinicaoSenha : React.FC = () => {
    const [email, setEmail] = useState('');

    const router = useRouter();

    const handleRequest = async () => {
        try
        {
            const response = await solicitarRedefinicaoSenha(email);
            if (response !== null)
            {
                Alert.alert("Sucesso", "Instruções para redefinição de senha foram enviadas para o seu e-mail.");
                router.push('/(auth)');
            }
        }
        catch (error) {
            console.error("Erro ao solicitar redefinição de senha:", error);
        }
    }

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Solicitar Redefinição de Senha</Text>
            <TextInput style={styles.input} placeholder='Seu E-mail' placeholderTextColor={Colors.light.textSecondary} value={email} onChangeText={setEmail}></TextInput>
            <TouchableOpacity onPress={handleRequest} style={styles.button}>
                <Text style={styles.buttonText}>Enviar Solicitação</Text>
            </TouchableOpacity>
        </View>
    );
}

const styles = StyleSheet.create({
    container: { flex: 1, justifyContent: 'center', padding: 20, backgroundColor: Colors.light.background },
    title: { fontSize: 24, fontWeight: 'bold', textAlign: 'center', marginBottom: 10, color: Colors.light.primaryDark },
    subtitle: { fontSize: 16, textAlign: 'center', color: Colors.light.textSecondary, marginBottom: 30 },
    input: { height: 50, borderColor: Colors.light.border, borderWidth: 1, borderRadius: 8, paddingHorizontal: 15, marginBottom: 15, backgroundColor: Colors.light.card, color: Colors.light.text, fontSize: 16 },
    button: { backgroundColor: Colors.light.primary, paddingVertical: 15, borderRadius: 8, alignItems: 'center', },
    buttonDisabled: { backgroundColor: Colors.light.primaryLight },
    buttonText: { color: Colors.light.textOnPrimary, fontSize: 16, fontWeight: 'bold', },
    backButton: { marginTop: 15, backgroundColor: Colors.light.backButton, paddingVertical: 15, borderRadius: 8, alignItems: 'center' },
    backButtonText: { color: Colors.light.textSecondary, textAlign: 'center', fontSize: 16, }
});

export default SolicitarRedefinicaoSenha;