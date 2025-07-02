import { useRouter } from 'expo-router';
import React, { useState } from 'react';
import { Alert, Button, StyleSheet, Text, TextInput, View } from 'react-native';
import { useAuth } from '../../context/AuthContext';

const PreCadastroScreen : React.FC = () => {
    const [cpf, setCpf] = useState('');
    const [email, setEmail] = useState('')
    const [isLoading, setIsLoading] = useState(false);
    const { checkPreCadastro } = useAuth();
    const router = useRouter();

    const handleVerify = async () => {
        if (!cpf) {
        Alert.alert("Erro", "Por favor, preencha seu CPF.");
        return;
        }
        setIsLoading(true);
        const result = await checkPreCadastro({ cpf });
        setIsLoading(false);

        if (result.success) {
        router.push('/(auth)/finalizar-cadastro');
        } else {
        Alert.alert('Verificação Falhou', result.error);
        }
    };

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Primeiro Acesso</Text>
            <Text style={styles.subtitle}>Informe o CPF utilizado na sua proposta para continuar.</Text>
            <TextInput style={styles.input} placeholder="Digite seu CPF" keyboardType="numeric" value={cpf} onChangeText={setCpf} />
            <Button title={isLoading ? "Verificando..." : "Continuar"} onPress={handleVerify} disabled={isLoading} />
            <Button title="Voltar para o Login" onPress={() => router.back()} color="gray" />
        </View>
    );
}

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', padding: 20, backgroundColor: '#fff' },
  title: { fontSize: 28, fontWeight: 'bold', textAlign: 'center', marginBottom: 10 },
  subtitle: { fontSize: 16, textAlign: 'center', color: 'gray', marginBottom: 30 },
  input: { height: 50, borderColor: '#ccc', borderWidth: 1, borderRadius: 8, paddingHorizontal: 15, marginBottom: 15 },
});

export default PreCadastroScreen;