import { useRouter } from 'expo-router';
import { useEffect, useState } from 'react';
import { Alert, Button, StyleSheet, Switch, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { useAuth } from '../../context/AuthContext';
import { Colors } from '@/constants/Colors';

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
            <TextInput style={styles.input} placeholder="Crie sua senha" value={senha} onChangeText={setSenha} secureTextEntry placeholderTextColor={Colors.light.textSecondary} />
            <TextInput style={styles.input} placeholder="Confirme sua senha" value={confirmarSenha} onChangeText={setConfirmarSenha} secureTextEntry placeholderTextColor={Colors.light.textSecondary} />
            
            <View style={styles.termsContainer}>
                <Switch 
                  value={aceitouTermos} 
                  onValueChange={setAceitouTermos}
                  thumbColor={aceitouTermos ? Colors.light.primary : '#f4f3f4'}
                  trackColor={{false: '#767577', true: Colors.light.primaryLight}}
                />
                <Text style={styles.termsText}>Eu li e aceito os Termos de Uso.</Text>
            </View>

            <TouchableOpacity onPress={handleFinish} disabled={isLoading} style={styles.button}>
                <Text style={styles.buttonText}>
                    {
                        isLoading ? "Finalizando..." : "Concluir Cadastro"
                    }
                </Text>
            </TouchableOpacity>
        </View>
    );
}

const styles = StyleSheet.create({
    container: { flex: 1, justifyContent: 'center', padding: 20, backgroundColor: Colors.light.background },
    title: { fontSize: 26, fontWeight: 'bold', textAlign: 'center', marginBottom: 10, color: Colors.light.primaryDark },
    subtitle: { fontSize: 16, textAlign: 'center', color: Colors.light.textSecondary, marginBottom: 30 },
    input: { height: 50, borderColor: Colors.light.border, borderWidth: 1, borderRadius: 8, paddingHorizontal: 15, marginBottom: 15, backgroundColor: Colors.light.card, color: Colors.light.text, fontSize: 16,},
    termsContainer: { flexDirection: 'row', alignItems: 'center', marginVertical: 20 },
    termsText: { flex: 1, marginLeft: 10, color: Colors.light.textSecondary },
    button: { backgroundColor: Colors.light.primary, paddingVertical: 15, borderRadius: 8, alignItems: 'center', },
    buttonDisabled: { backgroundColor: Colors.light.primaryLight },
    buttonText: { color: Colors.light.textOnPrimary, fontSize: 16, fontWeight: 'bold', }
});

