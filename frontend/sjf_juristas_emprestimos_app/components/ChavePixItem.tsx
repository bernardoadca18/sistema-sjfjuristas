import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { ChavePix } from '@/types/Cliente';
import { Colors } from '@/constants/Colors';

interface ChavePixItemProps
{
    item: ChavePix;
    onSetAtiva: (id: string) => void;
    onDelete: (item: ChavePix) => void;
    hideActions?: boolean;
}

export const ChavePixItem: React.FC<ChavePixItemProps> = ({ item, onSetAtiva, onDelete, hideActions = false }) => {
    return (
        <View style={[styles.chaveItem, styles.card]}>
            <View style={styles.chaveInfo}>
                <Text style={styles.chaveTipo}>{item.tipoChavePixNome}</Text>
                <Text style={styles.chaveValor}>{item.valorChaveMascarado}</Text>
                {
                    item.ativaParaDesembolso && (
                        <Text style={styles.chaveAtiva}>✓ Padrão</Text>
                    )
                }
            </View>

            {
                !hideActions && (
                    <View style={styles.chaveAcoes}>
                    {
                        !item.ativaParaDesembolso && (
                            <TouchableOpacity onPress={() => onSetAtiva(item.id)} style={styles.acaoButton}>
                            <Ionicons name="checkmark-circle-outline" size={24} color={Colors.light.primary} />
                            </TouchableOpacity>
                        )
                    }
                    <TouchableOpacity onPress={() => onDelete(item)} style={styles.acaoButton}>
                        <Ionicons name="trash-outline" size={24} color={Colors.light.atrasadoText} />
                    </TouchableOpacity>
                    </View>
                )
            }
        </View>
    );
}

const styles = StyleSheet.create(
{
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
    card: 
    {
        backgroundColor: Colors.light.card,
        borderRadius: 16,
        padding: 20,
        marginBottom: 20,
        alignItems: 'center',
        shadowColor: "#000",
        shadowOffset: {
            width: 0,
            height: 2
        },
        shadowOpacity: 0.1,
        shadowRadius: 3.84,
        elevation: 5,
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
});