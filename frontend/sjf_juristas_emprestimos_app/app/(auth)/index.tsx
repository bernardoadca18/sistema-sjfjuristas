import { useRouter } from 'expo-router';
import React, { useState } from 'react';
import { Alert, Button, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { useAuth } from '../../context/AuthContext';

const LoginScreen : React.FC = () => {
    const [email, setEmail] = useState('');
    const [senha, setSenha] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const { signIn } = useAuth();
    const router = useRouter();

    const handleLogin = async () => {
        if (!email || !senha)
        {
            Alert.alert("Erro", "Por favor, preencha e-mail e senha.");
            return;
        }
        setIsLoading(true);
        const result = await signIn({ email, senha });
        setIsLoading(false);

        if (!result.success)
        {
            Alert.alert("Erro no Login", result.error);
        }
    }

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Bem-vindo à SJF Juristas</Text>
            <TextInput style={styles.input} placeholder="E-mail" value={email} onChangeText={setEmail} keyboardType="email-address" autoCapitalize="none" />
            <TextInput style={styles.input} placeholder="Senha" value={senha} onChangeText={setSenha} secureTextEntry />
            <Button title={isLoading ? "Entrando..." : "Entrar"} onPress={handleLogin} disabled={isLoading} />

            <View style={styles.separator}/>

            <Text style={styles.subtitle}>É seu primeiro acesso ?</Text>
            <TouchableOpacity onPress={() => router.push('/(auth)/pre-cadastro')}>
                <Text>Finalizar meu Cadastro</Text>
            </TouchableOpacity>
        </View>
    )
}

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', padding: 20, backgroundColor: '#fff' },
  title: { fontSize: 28, fontWeight: 'bold', textAlign: 'center', marginBottom: 30 },
  subtitle: { fontSize: 16, textAlign: 'center', color: 'gray', marginBottom: 10 },
  input: { height: 50, borderColor: '#ccc', borderWidth: 1, borderRadius: 8, paddingHorizontal: 15, marginBottom: 15 },
  separator: { height: 1, width: '80%', backgroundColor: '#eee', marginVertical: 30, alignSelf: 'center' },
  link: { color: '#007AFF', fontSize: 16, textAlign: 'center', fontWeight: 'bold' }
});

export default LoginScreen;