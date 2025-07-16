import React, { useState, useEffect } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet, Alert, ActivityIndicator, ScrollView } from 'react-native';
import { Stack, useRouter } from 'expo-router';
import { Colors } from '@/constants/Colors';
import { getPerfil, updatePerfil } from '@/services/clienteService';
import { Cliente, ClienteUpdateRequest } from '@/types/Cliente';
import { formatTelefone } from '@/utils/formatTelefone';

const EditarCadastroScreen : React.FC = () => {
    const router = useRouter();
    const [isLoading, setIsLoading] = useState(true);
    const [isSaving, setIsSaving] = useState(false);
    
    const [nome, setNome] = useState('');
    const [telefone, setTelefone] = useState('');
    const [endereco, setEndereco] = useState('');

    useEffect(() => {
        const carregarDados = async () => {
            try
            {
                const dados = await getPerfil();
                setNome(dados.nomeCompleto);
                setTelefone(dados.telefoneWhatsapp);
                setEndereco('');
            }
            catch (error)
            {
                Alert.alert('Erro', 'Não foi possível carregar seus dados.');
                console.error(error);
            }
            finally
            {
                setIsLoading(false);
            }
        };
        carregarDados();
    }, []);

    const handleSalvar = async () => {
        const dadosParaAtualizar: ClienteUpdateRequest = {
            nomeCompleto: nome,
            telefoneWhatsapp: telefone.replace(/\D/g, ''),
            enderecoCompleto: endereco,
        };

        setIsSaving(true);
        try
        {
            await updatePerfil(dadosParaAtualizar);
            Alert.alert('Sucesso!', 'Seus dados foram atualizados.');
        } 
        catch (error) 
        {
            Alert.alert('Erro', 'Não foi possível salvar as alterações.');
            console.error(error);
        } 
        finally {
            setIsSaving(false);
        }
    };

    if (isLoading)
    {
        return <ActivityIndicator style={{ flex: 1 }} size="large" color={Colors.light.primary} />;
    }

    return (
        <ScrollView style={styles.container}>
            <Stack.Screen options={{ title: 'Editar Cadastro' }} />
            <Text style={styles.pageTitle}>Edite seus Dados</Text>

            <View style={styles.formGroup}>
                <Text style={styles.label}>Nome Completo</Text>
                <TextInput
                    style={styles.input}
                    value={nome}
                    onChangeText={setNome}
                    placeholder="Seu nome completo"
                    placeholderTextColor={Colors.light.textSecondary}
                />
            </View>

            <View style={styles.formGroup}>
                <Text style={styles.label}>Telefone / WhatsApp</Text>
                <TextInput
                    style={styles.input}
                    value={formatTelefone(telefone)}
                    onChangeText={setTelefone}
                    placeholder="(XX) XXXXX-XXXX"
                    keyboardType="phone-pad"
                    maxLength={16}
                    placeholderTextColor={Colors.light.textSecondary}
                />
            </View>

            <View style={styles.formGroup}>
                <Text style={styles.label}>Endereço Completo</Text>
                <TextInput
                    style={[styles.input, { height: 100 }]}
                    value={endereco}
                    onChangeText={setEndereco}
                    placeholder="Rua, número, bairro, cidade..."
                    multiline
                    placeholderTextColor={Colors.light.textSecondary}
                />
            </View>

            <View style={styles.buttonContainer}>
                <TouchableOpacity
                    style={[styles.button, isSaving && styles.buttonDisabled]}
                    onPress={handleSalvar}
                    disabled={isSaving}
                >
                    {isSaving ? (
                        <ActivityIndicator color="#fff" />
                    ) : (
                        <Text style={styles.buttonText}>Salvar Alterações</Text>
                    )}
                </TouchableOpacity>
                <TouchableOpacity
                    style={[styles.button, styles.backButton]}
                    onPress={() => {router.back()}}
                    disabled={isSaving}
                >
                    <Text style={styles.buttonText}>Voltar</Text>
                </TouchableOpacity>
            </View>
        </ScrollView>
    );

}

const styles = StyleSheet.create({
    container: { flex: 1, backgroundColor: Colors.light.background, padding: 20 },
    pageTitle: { fontSize: 24, fontWeight: 'bold', color: Colors.light.primaryDark, textAlign: 'center', marginBottom: 30 },
    formGroup: { marginBottom: 20 },
    label: { fontSize: 16, color: Colors.light.textSecondary, marginBottom: 8 },
    input: { height: 50, borderColor: Colors.light.border, borderWidth: 1, borderRadius: 8, paddingHorizontal: 15, backgroundColor: Colors.light.card, fontSize: 16 },
    button: { backgroundColor: Colors.light.primary, paddingVertical: 15, borderRadius: 8, alignItems: 'center' },
    buttonDisabled: { backgroundColor: Colors.light.primaryLight },
    buttonText: { color: Colors.light.textOnPrimary, fontSize: 16, fontWeight: 'bold' },
    backButton: { backgroundColor : Colors.light.backButton },
    buttonContainer: {gap: 16}
});

export default EditarCadastroScreen;