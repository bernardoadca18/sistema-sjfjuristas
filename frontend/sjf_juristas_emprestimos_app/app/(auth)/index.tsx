import { useRouter } from 'expo-router';
import React, { useState } from 'react';
import { Alert, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { useAuth } from '../../context/AuthContext';
import { Colors } from '@/constants/Colors';

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
            Alert.alert("Erro no Login");
        }
    }

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Bem-vindo à SJF Juristas</Text>
            <TextInput style={styles.input} placeholder="E-mail" value={email} onChangeText={setEmail} keyboardType="email-address" autoCapitalize="none" placeholderTextColor={Colors.light.textSecondary}/>
            <TextInput style={styles.input} placeholder="Senha" value={senha} onChangeText={setSenha} secureTextEntry placeholderTextColor={Colors.light.textSecondary}/>
            <TouchableOpacity style={[styles.button, isLoading && styles.buttonDisabled]} onPress={handleLogin} disabled={isLoading}>
                <Text style={styles.buttonText}>{isLoading ? "Entrando..." : "Entrar"}</Text>
            </TouchableOpacity>

            <View style={styles.separator}/>

            <Text style={styles.subtitle}>É seu primeiro acesso ?</Text>
            <TouchableOpacity onPress={() => router.push('/(auth)/pre-cadastro')}>
                <Text style={styles.link}>Finalizar meu Cadastro</Text>
            </TouchableOpacity>
        </View>
    )
}

const styles = StyleSheet.create({
    container: { flex: 1, justifyContent: 'center', padding: 20, backgroundColor: Colors.light.background },
    title: { fontSize: 28, fontWeight: 'bold', textAlign: 'center', marginBottom: 30, color: Colors.light.primaryDark },
    subtitle: { fontSize: 16, textAlign: 'center', color: Colors.light.textSecondary, marginBottom: 10 },
    input: { height: 50, borderColor: Colors.light.border, borderWidth: 1, borderRadius: 8, paddingHorizontal: 15, marginBottom: 15, backgroundColor: Colors.light.card, color: Colors.light.text, fontSize: 16, },
    separator: { height: 1, width: '80%', backgroundColor: Colors.light.border, marginVertical: 30, alignSelf: 'center' },
    link: { color: Colors.light.primary, fontSize: 16, textAlign: 'center', fontWeight: 'bold' },
    button: { backgroundColor: Colors.light.primary, paddingVertical: 15, borderRadius: 8, alignItems: 'center', },
    buttonDisabled: { backgroundColor: Colors.light.primaryLight, },
    buttonText: { color: Colors.light.textOnPrimary, fontSize: 16, fontWeight: 'bold', },
    buttonEntrar: {alignSelf: 'center', backgroundColor: Colors.light.primary, paddingVertical: 15, borderRadius: 8, alignItems: 'center', textAlign: 'center'}
});

export default LoginScreen;