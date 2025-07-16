import React, { useState, useEffect } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet, Alert, ActivityIndicator } from 'react-native';
import { useAuth } from '../../context/AuthContext';
import { CheckBox, Switch } from 'react-native-elements';
import { useRouter } from 'expo-router';
import api  from '../../services/api';
import { Colors } from '@/constants/Colors';
import { addChavePix, getTiposChavePix } from '@/services/clienteService';
import { TipoChavePix } from '@/types/Cliente';

export default function FinalizarCadastroScreen() {
    const { completeRegistration, onboardingData } = useAuth();
    const router = useRouter();
    
    const [step, setStep] = useState(1);
    
    const [senha, setSenha] = useState('');
    const [confirmarSenha, setConfirmarSenha] = useState('');
    const [aceitouTermos, setAceitouTermos] = useState(false);
    
    const [tiposChave, setTiposChave] = useState<TipoChavePix[]>([]);
    const [selectedTipo, setSelectedTipo] = useState<TipoChavePix | null>(null);
    const [newChaveValor, setNewChaveValor] = useState('');
    const [isPixAdded, setIsPixAdded] = useState(false);
    
    const [isLoading, setIsLoading] = useState(false);
    const [isAddingPix, setIsAddingPix] = useState(false);
    
    useEffect(() => {
        if (!onboardingData)
        {
            router.replace('/(auth)/pre-cadastro');
            return;
        }

        const fetchTiposChave = async () => {
            try 
            {
                const response = await getTiposChavePix();
                setTiposChave(response);
            } 
            catch (error) 
            {
                console.error("Erro ao buscar tipos de chave:", error);
            }
        };

        fetchTiposChave();

    }, [onboardingData, router]);

    const handleContinueToPixStep = () => {
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
        setStep(2);
    };

    const handleAddChave = async () => {
        if (!selectedTipo || !newChaveValor)
        {
            Alert.alert('Campo Obrigatório', 'Selecione um tipo de chave e preencha o valor.');
            return;
        }
        setIsAddingPix(true);

        try
        {
            await addChavePix(selectedTipo.id.toString(), newChaveValor, onboardingData!.usuarioId);
            
            setIsPixAdded(true);
            Alert.alert('Sucesso!', 'Chave PIX adicionada. Agora clique em "Finalizar Cadastro".');
            setNewChaveValor('');
            setSelectedTipo(null);
        }
        catch (error: any)
        {
            Alert.alert('Erro ao Adicionar');
        }
        finally
        {
            setIsAddingPix(false);
        }
    }

    const handleFinishRegistration = async () => {
        if (!isPixAdded) {
            Alert.alert('Cadastro Incompleto', 'É obrigatório adicionar uma chave PIX para finalizar.');
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
            Alert.alert('Erro');
        }
    }
    
    if (!onboardingData) return <ActivityIndicator size="large" color="#0000ff" />;

    return (
        <View style={styles.container}>
            <Text style={styles.title}>
                {step === 1 ? 'Crie sua Senha' : 'Cadastre uma Chave PIX'}
            </Text>
            <Text style={styles.subtitle}>
                {step === 1 
                    ? 'Estamos quase lá! Defina uma senha segura para acessar sua conta.' 
                    : 'Para receber seus empréstimos, é obrigatório cadastrar uma chave PIX.'}
            </Text>
            
            {
                step === 1 && (
                    <>
                        <TextInput style={styles.input} placeholder="Senha" value={senha} onChangeText={setSenha} secureTextEntry placeholderTextColor={Colors.light.textSecondary} />
                        <TextInput style={styles.input} placeholder="Confirmar Senha" value={confirmarSenha} onChangeText={setConfirmarSenha} secureTextEntry placeholderTextColor={Colors.light.textSecondary}/>
                        <View style={styles.checkboxContainer}>
                            <CheckBox checked={aceitouTermos} onPress={() => setAceitouTermos(!aceitouTermos)} containerStyle={[styles.checkbox]} checkedColor={Colors.light.primary} />
                            <Text style={styles.label}>Li e aceito os termos de uso.</Text>
                        </View>
                        <TouchableOpacity style={styles.button} onPress={handleContinueToPixStep}>
                            <Text style={styles.buttonText}>Continuar</Text>
                        </TouchableOpacity>
                    </>
                )
            }
            {
                step === 2 && (
                    <>
                        <Text style={styles.fieldLabel}>Selecione o tipo de chave:</Text>
                        <View style={styles.tipoChaveContainer}>
                            {tiposChave.map((tipo) => (
                                <TouchableOpacity
                                    key={tipo.id}
                                    style={[
                                        styles.tipoChaveButton,
                                        selectedTipo?.id === tipo.id && styles.tipoChaveSelected
                                    ]}
                                    onPress={() => setSelectedTipo(tipo)}
                                >
                                    <Text style={[
                                        styles.tipoChaveText,
                                        selectedTipo?.id === tipo.id && styles.tipoChaveTextSelected
                                    ]}>{tipo.nomeTipo}</Text>
                                </TouchableOpacity>
                            ))}
                        </View>

                        {selectedTipo && (
                            <>
                                <TextInput
                                    style={styles.input}
                                    placeholder={`Digite sua chave ${selectedTipo.nomeTipo}`}
                                    value={newChaveValor}
                                    onChangeText={setNewChaveValor}
                                    keyboardType={selectedTipo.nomeTipo === 'CPF' ? 'numeric' : 'default'}
                                    placeholderTextColor={Colors.light.textSecondary}
                                />
                                <TouchableOpacity style={styles.addPixButton} onPress={handleAddChave} disabled={isAddingPix}>
                                    {isAddingPix ? <ActivityIndicator color="#fff" /> : <Text style={styles.buttonText}>Adicionar Chave</Text>}
                                </TouchableOpacity>
                            </>
                        )}
                        
                        <View style={styles.divider} />

                        <TouchableOpacity 
                            style={[styles.button, !isPixAdded && styles.disabledButton]} 
                            onPress={handleFinishRegistration} 
                            disabled={!isPixAdded || isLoading}
                        >
                            {isLoading ? <ActivityIndicator color="#fff" /> : <Text style={styles.buttonText}>Finalizar Cadastro</Text>}
                        </TouchableOpacity>
                    </>
                )
            }
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
