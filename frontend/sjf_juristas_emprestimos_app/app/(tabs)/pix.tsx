import React, { useState, useEffect, useCallback } from 'react';
import { View, Text, StyleSheet, FlatList, TouchableOpacity, ActivityIndicator, Alert, Modal, TextInput, ScrollView } from 'react-native';
import { Stack } from 'expo-router';
import { Colors } from '@/constants/Colors';
import { ChavePix, TipoChavePix } from '@/types/Cliente';
import { getChavesPix, addChavePix, setChavePixAtiva, deleteChavePix, getTiposChavePix } from '@/services/clienteService';
import { Ionicons } from '@expo/vector-icons';
import { ChavePixItem } from '../../components/ChavePixItem';

const PixManagementScreen = () => {
    const [chaves, setChaves] = useState<ChavePix[]>([]);
    const [tiposChave, setTiposChave] = useState<TipoChavePix[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [modalVisible, setModalVisible] = useState(false);
    const [newChaveValor, setNewChaveValor] = useState('');
    const [selectedTipo, setSelectedTipo] = useState<TipoChavePix | undefined>();
    const [pickerModalVisible, setPickerModalVisible] = useState(false);

    const fetchData = useCallback(async () => {
        try
        {
            setIsLoading(true);
            const [chavesData, tiposData] = await Promise.all([getChavesPix(), getTiposChavePix()]);

            setChaves(chavesData);
            setTiposChave(tiposData);

            if (tiposData.length > 0)
            {
                setSelectedTipo(tiposData[0]);
            }
        }
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        catch (error : any)
        {
            Alert.alert('Erro', `Não foi possível carregar os dados das chaves PIX.`);
        }
        finally
        {
            setIsLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchData();
    }, [fetchData]);

    const handleAction = async (action: () => Promise<any>, successMessage: string, errorMessage: string) => {
        try {
            await action();
            Alert.alert('Sucesso', successMessage);
            fetchData();
        } catch (error: any) {
            Alert.alert('Erro', `${errorMessage}: ${error.message}`);
        }
    };

    const handleSetAtiva = async (id: string) => {
        await setChavePixAtiva(id);
        Alert.alert('Sucesso', 'Chave PIX definida como padrão para recebimento');
        fetchData();
    };

    const handleDelete = (item: ChavePix) => {
        Alert.alert('Confirmar Exclusão', `Tem certeza que deseja excluir a chave PIX "${item.valorChaveMascarado}"?`,
            [{ text: 'Cancelar', style: 'cancel' }, { text: 'Excluir', style: 'destructive', onPress: () => handleAction(() => deleteChavePix(item.id), 'Chave PIX excluída.', 'Não foi possível excluir a chave') }]
        );
    };

    const handleAddChave = async () => {
        if (!selectedTipo || !newChaveValor)
        {
            Alert.alert('Erro', 'Por favor, selecione um tipo e informe o valor da chave.');
            return;
        }

        handleAction(() => addChavePix(selectedTipo.id, newChaveValor), 'Nova chave PIX adicionada!', 'Não foi possível adicionar a nova chave');
        setNewChaveValor('');
        setModalVisible(false);
    };

    const renderItem = ({ item } : { item : ChavePix }) => (
        <ChavePixItem item={item} onSetAtiva={handleSetAtiva} onDelete={handleDelete} />
    );

    if (isLoading)
    {
        return (
            <View style={styles.centered}>
                <ActivityIndicator size="large" color={Colors.light.primary} />
            </View>
        )
    }

    return (
        <View style={styles.container}>
            <Stack.Screen options={{title: 'Chaves PIX'}} />
            <Text style={styles.pageTitle}>Chaves PIX</Text>
            <FlatList
                data={chaves}
                renderItem={renderItem}
                keyExtractor={ (item) => item.id }
                ListEmptyComponent={<Text style={styles.emptyText}>Nenhuma chave PIX cadastrada.</Text>}
                contentContainerStyle={{ paddingBottom: 100 }}
            />

            <TouchableOpacity style={styles.addButton} onPress={() => setModalVisible(true)}>
                <Ionicons name="add" size={32} color="white" />
            </TouchableOpacity>

            <Modal animationType='slide' transparent={true} visible={modalVisible} onRequestClose={() => setModalVisible(false)}>
                <View style={styles.modalContainer}>
                    <View style={styles.modalView}>
                        <Text style={styles.modalTitle}>Adicionar Nova Chave PIX</Text>
                        
                        <TouchableOpacity style={styles.pickerButton} onPress={() => setPickerModalVisible(true)}>
                            <Text style={styles.pickerButtonText}>{selectedTipo ? selectedTipo.nomeTipo : "Selecione um tipo de chave"}</Text>
                            <Ionicons name="chevron-down" size={20} color="#666" />
                        </TouchableOpacity>

                        <TextInput style={styles.input} placeholder="Digite o valor da chave" value={newChaveValor} onChangeText={setNewChaveValor}/>

                        <View style={styles.modalButtons}>
                            <TouchableOpacity style={[styles.button, styles.buttonClose]} onPress={() => setModalVisible(false)}>
                                <Text style={styles.textStyle}>Cancelar</Text>
                            </TouchableOpacity>

                            <TouchableOpacity style={[styles.button, styles.buttonAdd]} onPress={handleAddChave}>
                                <Text style={styles.textStyle}>Salvar</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                </View>
            </Modal>

            <Modal animationType="slide" transparent={true} visible={pickerModalVisible} onRequestClose={() => setPickerModalVisible(false)}>
                <TouchableOpacity style={styles.modalContainer} activeOpacity={1} onPressOut={() => setPickerModalVisible(false)}>
                    <View style={styles.pickerModalView}>
                        <Text style={styles.modalTitle}>Selecione o Tipo de Chave</Text>
                        <ScrollView style={{width: '100%'}}>
                            {tiposChave.map(tipo => (
                                <TouchableOpacity key={tipo.id} style={styles.pickerItem} onPress={() => {
                                    setSelectedTipo(tipo);
                                    setPickerModalVisible(false);
                                }}>
                                    <Text style={styles.pickerItemText}>{tipo.nomeTipo}</Text>
                                </TouchableOpacity>
                            ))}
                        </ScrollView>
                    </View>
                </TouchableOpacity>
            </Modal>
        </View>
    );
};

export default PixManagementScreen;

const styles = StyleSheet.create(
{
    container:
    {
        flex: 1,
        backgroundColor: Colors.light.background,
        padding: 10
    },
    centered:
    {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center'
    },
    chaveItem: 
    {
        backgroundColor: 'white',
        padding: 15,
        borderRadius: 10,
        marginBottom: 10,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        elevation: 2,
    },
    chaveInfo: 
    {
        flex: 1
    },
    chaveTipo:
    {
        fontSize: 16,
        fontWeight: 'bold',
        color: Colors.light.text
    },
    chaveValor: 
    {
        fontSize: 14,
        color: Colors.light.textSecondary,
        marginTop: 4
    },
    chaveAtiva: 
    {
        fontSize: 12,
        color: Colors.light.pagoText,
        fontWeight: 'bold',
        marginTop: 4
    },
    chaveAcoes: 
    {
        flexDirection: 'row'
    },
    acaoButton: 
    {
        padding: 8
    },
    addButton: 
    {
        position: 'absolute',
        bottom: 30,
        right: 30,
        width: 60,
        height: 60,
        borderRadius: 30,
        backgroundColor: Colors.light.primary,
        justifyContent: 'center',
        alignItems: 'center',
        elevation: 8,
    },
    emptyText:
    {
        textAlign: 'center',
        marginTop: 50,
        color: Colors.light.textSecondary
    },
    modalContainer: 
    {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0,0,0,0.5)',
    },
    modalView: 
    {
        margin: 20,
        backgroundColor: 'white',
        borderRadius: 20,
        padding: 35,
        alignItems: 'center',
        elevation: 5,
        width: '90%',
    },
    modalTitle: 
    {
        marginBottom: 15,
        textAlign: 'center',
        fontSize: 20,
        fontWeight: 'bold',
    },
    input:
    {
        height: 50,
        width: '100%',
        borderColor: Colors.light.border,
        borderWidth: 1,
        borderRadius: 8,
        paddingHorizontal: 15,
        marginBottom: 15,
        backgroundColor: Colors.light.card,
    },
    picker:
    {
        width: '100%',
        height: 50,
        marginBottom: 15,
    },
    modalButtons:
    {
        flexDirection: 'row',
        justifyContent: 'space-between',
        width: '100%',
    },
    button:
    {
        borderRadius: 10,
        padding: 10,
        elevation: 2,
        flex: 1,
        marginHorizontal: 5,
    },
    buttonAdd:
    {
        backgroundColor: Colors.light.primary,
    },
    buttonClose:
    {
        backgroundColor: Colors.light.textSecondary,
    },
    textStyle:
    {
        color: 'white',
        fontWeight: 'bold',
        textAlign: 'center',
    },
    pickerButton:
    {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        height: 50,
        width: '100%',
        borderColor: Colors.light.border,
        borderWidth: 1, borderRadius: 8, paddingHorizontal: 15, marginBottom: 15, backgroundColor: Colors.light.card
    },
    pickerButtonText: { fontSize: 16, color: '#333' },
    pickerModalView: { width: '80%', maxHeight: '50%', backgroundColor: 'white', borderRadius: 10, padding: 20, alignItems: 'center', elevation: 10 },
    pickerItem: { paddingVertical: 15, borderBottomWidth: 1, borderBottomColor: '#eee', width: '100%' },
    pickerItemText: { textAlign: 'center', fontSize: 18 },
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold',
        color: Colors.light.primaryDark,
        textAlign: 'center',
        marginVertical: 20,
    },

});