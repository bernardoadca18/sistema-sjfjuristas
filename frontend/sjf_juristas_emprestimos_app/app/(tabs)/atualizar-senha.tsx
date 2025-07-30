import { useRouter } from 'expo-router';
import React, { useState } from 'react';
import { Alert, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { Colors } from '@/constants/Colors';
import { redefinirSenha } from '@/services/senhaService';


const AtualizarSenha : React.FC = () => {
    const [senhaAtual, setSenhaAtual] = useState('');
    const [novaSenha, setNovaSenha] = useState('');
    const [confirmarNovaSenha, setConfirmarNovaSenha] = useState('');


    const router = useRouter();

    const handleRequest = async () => {
        if (!senhaAtual || !novaSenha || !confirmarNovaSenha)
        {
            Alert.alert("Erro", "Por favor, preencha todos os campos.");
            return;
        }
        if (novaSenha !== confirmarNovaSenha)
        {
            Alert.alert("Erro", "As novas senhas não coincidem.");
            return;
        }

        if (novaSenha.length < 8)
        {
            Alert.alert("Erro", "A nova senha deve ter pelo menos 8 caracteres.");
            return;
        }

        try
        {
            const response = await redefinirSenha(senhaAtual, novaSenha, confirmarNovaSenha);
            if (response !== null)
            {
                Alert.alert("Sucesso", "Senha atualizada com sucesso.");
                router.push('/(tabs)');
            }
        }
        catch (error)
        {
            console.error("Erro ao solicitar redefinição de senha:", error);
        }
    }

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Atualize sua Senha</Text>
            <TextInput secureTextEntry style={styles.input} placeholder='Senha Atual' placeholderTextColor={Colors.light.textSecondary} value={senhaAtual} onChangeText={setSenhaAtual}></TextInput>
            <TextInput secureTextEntry style={styles.input} placeholder='Nova Senha' placeholderTextColor={Colors.light.textSecondary} value={novaSenha} onChangeText={setNovaSenha}></TextInput>
            <TextInput secureTextEntry style={styles.input} placeholder='Confirmar Nova Senha' placeholderTextColor={Colors.light.textSecondary} value={confirmarNovaSenha} onChangeText={setConfirmarNovaSenha}></TextInput>
            <TouchableOpacity onPress={handleRequest} style={styles.button}>
                <Text style={styles.buttonText}>Atualizar</Text>
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

export default AtualizarSenha;