import React, { useState, useEffect } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet, Alert, ActivityIndicator } from 'react-native';
import { useAuth } from '../../context/AuthContext';
import { CheckBox } from 'react-native-elements';
import { useRouter } from 'expo-router';
import { Colors } from '@/constants/Colors';

export default function FinalizarCadastroScreen() {
    const { completeRegistration, onboardingData } = useAuth();
    const router = useRouter();
    
    const [senha, setSenha] = useState('');
    const [confirmarSenha, setConfirmarSenha] = useState('');
    const [aceitouTermos, setAceitouTermos] = useState(false);
    
    const [isLoading, setIsLoading] = useState(false);
    
    useEffect(() => {
        if (!onboardingData)
        {
            router.replace('/(auth)/pre-cadastro');
            return;
        }

    }, [onboardingData, router]);


    const handleFinishRegistration = async () => {
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
            Alert.alert('Erro');
        }
    }
    
    if (!onboardingData) return <ActivityIndicator size="large" color="#0000ff" />;

    return (
        <View style={styles.container}>
            <Text style={styles.title}>
                {'Crie sua Senha'}
            </Text>
            <Text style={styles.subtitle}>
                {'Estamos quase lá! Defina uma senha segura para acessar sua conta.' }
            </Text>
            
            <>
                <TextInput style={styles.input} placeholder="Senha" value={senha} onChangeText={setSenha} secureTextEntry placeholderTextColor={Colors.light.textSecondary} />
                <TextInput style={styles.input} placeholder="Confirmar Senha" value={confirmarSenha} onChangeText={setConfirmarSenha} secureTextEntry placeholderTextColor={Colors.light.textSecondary}/>
                <View style={styles.checkboxContainer}>
                    <CheckBox checked={aceitouTermos} onPress={() => setAceitouTermos(!aceitouTermos)} containerStyle={[styles.checkbox]} checkedColor={Colors.light.primary} />
                    <Text style={styles.label}>Li e aceito os termos de uso.</Text>
                </View>
                <TouchableOpacity style={styles.button} onPress={handleFinishRegistration} disabled={isLoading}>
                    <Text style={styles.buttonText}>Continuar</Text>
                </TouchableOpacity>
            </>

        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        padding: 20,
        backgroundColor: Colors.light.background,
    },
    title: {
        fontSize: 26,
        fontWeight: 'bold',
        marginBottom: 10,
        textAlign: 'center',
        color: Colors.light.text,
    },
    subtitle: {
        fontSize: 16,
        textAlign: 'center',
        marginBottom: 30,
        color: Colors.light.textSecondary,
    },
    input: {
        height: 50,
        borderColor: Colors.light.border,
        borderWidth: 1,
        borderRadius: 8,
        marginBottom: 15,
        paddingHorizontal: 15,
        fontSize: 16,
        color: Colors.light.text,
        backgroundColor: Colors.light.card,
    },
    button: {
        backgroundColor: Colors.light.primary,
        paddingVertical: 15,
        borderRadius: 8,
        alignItems: 'center',
        marginTop: 10,
    },
    buttonText: {
        color: Colors.light.textOnPrimary,
        fontSize: 18,
        fontWeight: 'bold',
    },
    checkboxContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        marginBottom: 20,
        marginLeft: -10,
    },
    checkbox: {
        backgroundColor: 'transparent',
        borderWidth: 0,
        padding: 0,
    },
    label: {
        flex: 1,
        fontSize: 16,
        color: Colors.light.textSecondary,
    },
    fieldLabel: {
        fontSize: 16,
        color: Colors.light.text,
        marginBottom: 10,
    },
    tipoChaveContainer: {
        flexDirection: 'row',
        flexWrap: 'wrap',
        justifyContent: 'center',
        marginBottom: 20,
    },
    tipoChaveButton: {
        paddingVertical: 10,
        paddingHorizontal: 15,
        borderWidth: 1,
        borderColor: Colors.light.primary,
        borderRadius: 20,
        margin: 5,
    },
    tipoChaveSelected: {
        backgroundColor: Colors.light.primary,
    },
    tipoChaveText: {
        color: Colors.light.primary,
        fontSize: 14,
    },
    tipoChaveTextSelected: {
        color: Colors.light.textOnPrimary,
    },
    addPixButton: {
        backgroundColor: Colors.light.primaryDark,
        paddingVertical: 15,
        borderRadius: 8,
        alignItems: 'center',
    },
    disabledButton: {
        backgroundColor: Colors.light.textSecondary,
    },
    divider: {
        height: 1,
        backgroundColor: Colors.light.border,
        marginVertical: 30,
    },
});
