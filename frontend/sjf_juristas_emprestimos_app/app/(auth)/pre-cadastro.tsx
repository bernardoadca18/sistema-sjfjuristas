import { useRouter } from 'expo-router';
import React, { useState } from 'react';
import { Alert, Button, StyleSheet, Text, TextInput, View } from 'react-native';
import { useAuth } from '../../context/AuthContext';

const PreCadastroScreen : React.FC = () => {
    const [cpf, setCpf] = useState('');
    const [email, setEmail] = useState('');
    const [dataNascimento, setDataNascimento] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const { checkPreCadastro } = useAuth();
    const router = useRouter();

    const handleVerify = async () => {
        if (!cpf && (!email || !dataNascimento))
        {
            Alert.alert('Dados Incompletos', 'Por favor, informe o CPF ou o E-mail e a Data de Nascimento.');
            return;
        }

        setIsLoading(true);
        const result = await checkPreCadastro({ cpf, email, dataNascimento });
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
            
            <TextInput style={styles.input} placeholder="CPF" keyboardType="numeric" value={cpf} onChangeText={setCpf} />
            <TextInput style={styles.input} placeholder="E-mail" value={email} onChangeText={setEmail} keyboardType="email-address" autoCapitalize="none" />
            <TextInput style={styles.input} placeholder="Data de Nascimento (AAAA-MM-DD)" value={dataNascimento} onChangeText={setDataNascimento}/>
            
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