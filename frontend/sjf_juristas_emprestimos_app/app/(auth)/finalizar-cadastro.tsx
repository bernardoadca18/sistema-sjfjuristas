import { useRouter } from 'expo-router';
import { useEffect, useState } from 'react';
import { Alert, Button, StyleSheet, Switch, Text, TextInput, View } from 'react-native';
import { useAuth } from '../../context/AuthContext';

export default function FinalizarCadastroScreen() {
    const [senha, setSenha] = useState('');
    const [confirmarSenha, setConfirmarSenha] = useState('');
    const [aceitouTermos, setAceitouTermos] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const { completeRegistration, onboardingData } = useAuth();
    const router = useRouter();

    useEffect(() => {
        if (!onboardingData)
        {
            router.replace('/(auth)/pre-cadastro');
        }
    }, [onboardingData, router]);

    const handleFinish = async () => {
        if (senha !== confirmarSenha)
        {
            Alert.alert('Erro', 'As senhas não conferem.');
            return;
        }
        if (senha.length < 6)
        {
            Alert.alert('Erro', 'A senha deve ter no mínimo 6 caracteres.');
            return;
        }
        if (!aceitouTermos)
        {
            Alert.alert('Erro', 'Você precisa aceitar os Termos de Uso.');
            return;
        }

        setIsLoading(true);
        const result = await completeRegistration({
            usuarioId: onboardingData!.usuarioId,
            senha,
            confirmarSenha,
            aceitouTermosApp: aceitouTermos
        });
        setIsLoading(false);

        if (!result.success)
        {
            Alert.alert('Erro', result.error);
        }
    };

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Falta pouco, {onboardingData?.nomeCompleto}!</Text>
            <Text style={styles.subtitle}>Crie uma senha para acessar o aplicativo com segurança.</Text>
            <TextInput style={styles.input} placeholder="Crie sua senha" value={senha} onChangeText={setSenha} secureTextEntry />
            <TextInput style={styles.input} placeholder="Confirme sua senha" value={confirmarSenha} onChangeText={setConfirmarSenha} secureTextEntry />
            
            <View style={styles.termsContainer}>
                <Switch value={aceitouTermos} onValueChange={setAceitouTermos} />
                <Text style={styles.termsText}>Eu li e aceito os Termos de Uso.</Text>
            </View>

            <Button title={isLoading ? "Finalizando..." : "Concluir Cadastro"} onPress={handleFinish} disabled={isLoading} />
        </View>
    );
}

const styles = StyleSheet.create({
    container: { flex: 1, justifyContent: 'center', padding: 20, backgroundColor: '#fff' },
    title: { fontSize: 26, fontWeight: 'bold', textAlign: 'center', marginBottom: 10 },
    subtitle: { fontSize: 16, textAlign: 'center', color: 'gray', marginBottom: 30 },
    input: { height: 50, borderColor: '#ccc', borderWidth: 1, borderRadius: 8, paddingHorizontal: 15, marginBottom: 15 },
    termsContainer: { flexDirection: 'row', alignItems: 'center', marginVertical: 20 },
    termsText: { flex: 1, marginLeft: 10, color: 'gray' }
});