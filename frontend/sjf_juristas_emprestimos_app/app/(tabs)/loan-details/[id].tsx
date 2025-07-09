import React, { useCallback, useEffect, useState } from 'react';
import { View, Text, StyleSheet, ActivityIndicator, TouchableOpacity, FlatList } from 'react-native';
import { Stack, useLocalSearchParams } from 'expo-router';
import { Colors } from '@/constants/Colors';
import { Emprestimo, Parcela } from '@/types/Emprestimo';
import { getEmprestimoDetalhes, getParcelasPaged, getProximaParcela } from '@/services/emprestimoService';
import formatCurrency from '@/utils/formatCurrency';
import formatDate from '@/utils/formatDate';
import ParcelaWidget from '@/components/ui/ParcelaWidget';
import { Ionicons } from '@expo/vector-icons';

const LoanDetailsScreen : React.FC = () => {
    const { id } = useLocalSearchParams<{ id: string }>();

    const [emprestimo, setEmprestimo] = useState<Emprestimo | null>(null);
    const [parcelas, setParcelas] = useState<Parcela[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    const [isLoadingParcelas, setIsLoadingParcelas] = useState(false);

    const [proximaParcelaId, setProximaParcelaId] = useState<string | null>(null);

    const PAGE_SIZE = 20;


    const fetchEmprestimoData = useCallback(async (emprestimoId: string) => {
        try 
        {
            setIsLoading(true);
            setError(null);
            const emprestimoDetails = await getEmprestimoDetalhes(emprestimoId);
            setEmprestimo(emprestimoDetails);
        } 
        catch (err) 
        {
            setError('Não foi possível carregar os detalhes do contrato.');
            console.error(err);
        } 
        finally 
        {
            setIsLoading(false);
        }
    }, []);

    const fetchProximaParcelaData = useCallback(async (emprestimoId : string) => {
        try 
        {
            const proximaParcela = await getProximaParcela(emprestimoId);
            setProximaParcelaId(proximaParcela.id);
        } 
        catch (err: any) 
        {
            if (err.response && err.response.status === 404) 
            {
                setProximaParcelaId(null);
            } 
            else 
            {
                console.error('Erro em fetchProximaParcelaData:', err);
            }
        }
    }, [])

    const fetchParcelasData = useCallback(async (emprestimoId: string, page: number) => {
        try
        {
            setIsLoadingParcelas(true);
            const parcelasPage = await getParcelasPaged(emprestimoId, page, PAGE_SIZE);
            setParcelas(parcelasPage.content);
            setTotalPages(parcelasPage.totalPages);
            setCurrentPage(parcelasPage.number);
        } 
        catch (err) 
        {
            setError('Não foi possível carregar as parcelas.');
            console.error(err);
        } 
        finally 
        {
            setIsLoadingParcelas(false);
        }
    }, []);

    useEffect(() => {
        if (id)
        {
            fetchEmprestimoData(id);
            fetchProximaParcelaData(id);
            fetchParcelasData(id, 0);
        }
    }, [id, fetchEmprestimoData, fetchProximaParcelaData, fetchParcelasData]);

    const handlePageChange = (newPage : number) => {
        if (id && newPage >= 0 && newPage < totalPages)
        {
            fetchParcelasData(id, newPage);
        }
    }

    const renderInfoCard = (label: string, value: string | undefined, isCurrency: boolean = false) => (
        <View style={styles.infoCard}>
            <Text style={styles.infoLabel}>{ label }</Text>
            <Text style={styles.infoValue}>{ isCurrency && value ? formatCurrency(parseFloat(value)) : value || '-' }</Text>
        </View>
    );

    const renderPaginationControls = () => (
        <View style={styles.paginationContainer}>
            <TouchableOpacity style={[styles.paginationButton, currentPage === 0 && styles.disabledButton]} onPress={() => handlePageChange(currentPage - 1)} disabled={currentPage === 0}>
                <Ionicons name='arrow-back' size={20} color={"white"}/>
            </TouchableOpacity>
            
            <Text style={styles.paginationText}>{`Página ${currentPage + 1} de ${totalPages}`}</Text>

            <TouchableOpacity style={[styles.paginationButton, currentPage + 1 >= totalPages && styles.disabledButton]} onPress={() => handlePageChange(currentPage + 1)} disabled={currentPage + 1 >= totalPages}>
                <Ionicons name='arrow-forward' size={20} color={"white"}/>
            </TouchableOpacity>
        </View>
    );

    const Separator = () => <View style={styles.separator} />;

    const renderContent = () => {
        if (isLoading)
        {
            return <View style={styles.centered}><ActivityIndicator size={'large'} color={Colors.light.primary}/></View>
        }

        if (error && !emprestimo)
        {
            return (
                <View style={styles.centered}>
                    <Text style={styles.errorText}>{error}</Text>
                </View>
            );
        }

        return (
            <FlatList ListHeaderComponent={
                <>
                    <Text style={styles.sectionTitle}>Resumo do Contrato</Text>
                    <View style={styles.infoGrid}>
                        {renderInfoCard('Valor Total', emprestimo?.valorContratado.toString(), true)}
                        {renderInfoCard('Saldo Devedor Atual', emprestimo?.saldoDevedorAtual.toString(), true)}
                        {renderInfoCard('Valor Parcela Diária', emprestimo?.valorParcelaDiaria?.toString(), true)}
                        {renderInfoCard('Taxa de Juros (diária)', `${emprestimo?.taxaJurosDiariaEfetiva}%`)}
                        {renderInfoCard('Data de Aprovação', emprestimo?.dataContratacao ? formatDate(emprestimo.dataContratacao) : '-')}
                        {renderInfoCard('Total de Parcelas', emprestimo?.numeroTotalParcelas.toString())}
                    </View>
                </>
            }
            data={parcelas}
            keyExtractor={(item) => item.id.toString()}
            renderItem={({ item }) => (
                <ParcelaWidget
                    parcela={item}
                    isNextPayment={item.id === proximaParcelaId}
                />
            )}
            ListFooterComponent={totalPages > 1 ? renderPaginationControls() : null}
            contentContainerStyle={styles.container}
            ListEmptyComponent={!isLoadingParcelas ? <Text style={styles.infoText}>Nenhuma parcela encontrada.</Text> : null}
            ItemSeparatorComponent={Separator}
            />
        );
    };

    return (
        <View style={{ flex: 1, backgroundColor: Colors.light.background }}>
            <Stack.Screen options={{ title: "Detalhes do Empréstimo" }}/>
            { renderContent() }
        </View>
    );
};

const styles = StyleSheet.create({
    container: 
    {
        padding: 20,
        paddingBottom: 40,
    },
    centered: 
    {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        padding: 20,
    },
    sectionTitle: 
    {
        fontSize: 22,
        fontWeight: 'bold',
        color: Colors.light.primaryDark,
        marginBottom: 15,
        marginTop: 10,
    },
    infoGrid: 
    {
        flexDirection: 'row',
        flexWrap: 'wrap',
        justifyContent: 'space-between',
    },
    infoCard: 
    {
        backgroundColor: Colors.light.card,
        borderRadius: 10,
        padding: 15,
        width: '48%',
        marginBottom: 15,
        alignItems: 'center',
    },
    infoLabel: 
    {
        fontSize: 14,
        color: Colors.light.textSecondary,
        marginBottom: 5,
    },
    infoValue: 
    {
        fontSize: 18,
        fontWeight: 'bold',
        color: Colors.light.primary,
    },
    errorText: 
    {
        fontSize: 16,
        color: 'red',
        textAlign: 'center',
    },
    infoText:
    {
        textAlign: 'center',
        color: Colors.light.textSecondary,
        fontSize: 16,
        marginTop: 20,
    },
    paginationContainer:
    {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginTop: 20,
    },
    paginationButton:
    {
        backgroundColor: Colors.light.primary,
        padding: 10,
        borderRadius: 50,
    },
    disabledButton:
    {
        backgroundColor: Colors.light.border,
    },
    paginationText:
    {
        fontSize: 16,
        fontWeight: 'bold',
        color: Colors.light.text,
    },
    separator: 
    {
        height: 1,
        width: '100%',
        backgroundColor: '#d0d0d0',
    },
});

export default LoanDetailsScreen;