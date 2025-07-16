import EmprestimoHistoricoWidget from "@/components/ui/historico/EmprestimoHistoricoWidget";
import PropostaHistoricoItemWidget from "@/components/ui/historico/PropostaHistoricoItemWidget";
import PropostaHistoricoWidget from "@/components/ui/historico/PropostaHistoricoWidget";
import ParcelaWidget from "@/components/ui/ParcelaWidget";
import { Colors } from "@/constants/Colors";
import { getEmprestimosAtivos, getProximaParcela } from "@/services/emprestimoService";
import { getHistoricoEmprestimos, getHistoricoEmprestimosSummary, getHistoricoParcelas, getHistoricoPropostas, getMinhasPropostas } from "@/services/historicoService";
import { Emprestimo, EmprestimoSummary, Parcela } from "@/types/Emprestimo";
import { Page } from "@/types/Page";
import { Proposta, PropostaHistorico } from "@/types/Proposta";
import formatDate from "@/utils/formatDate";
import { Ionicons } from "@expo/vector-icons";
import { useLocalSearchParams, useRouter } from 'expo-router'; 
import React, { useCallback, useEffect, useState } from "react";
import { StyleSheet, Text, TouchableOpacity, View, FlatList, ActivityIndicator, Modal, Pressable } from "react-native";

type HistoryType = 'emprestimos' | 'parcelas_emprestimo' | 'propostas' | 'historico_proposta';

const HistoryScreen : React.FC = () => {
    const [historyType, setHistoryType] = useState<HistoryType>('emprestimos');
    const [data, setData] = useState<Emprestimo[] | Parcela[] | Proposta[] | PropostaHistorico[]>([]);
    
    const [isLoading, setIsLoading] = useState(false);
    const [isLoadingMore, setIsLoadingMore] = useState(false);
    
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);

    const [emprestimosSummary, setEmprestimosSummary] = useState<EmprestimoSummary[]>([]);
    const [selectedEmprestimoId, setSelectedEmprestimoId] = useState<string | null>(null);
    
    //const [propostas, setPropostas] = useState<Proposta[]>([]);
    //const [parcelas, setParcelas] = useState<Parcela[]>([]);
    //const [historicoProposta, setHistoricoProposta] = useState<PropostaHistorico[]>([]);
    //const [selectedPropostaId, setSelectedPropostaId] = useState<string | null>(null);
    
    
    const [pickerVisible, setPickerVisible] = useState(false);
    const [pickerOptions, setPickerOptions] = useState<{ label: string, value: string }[]>([]);
    const [pickerTitle, setPickerTitle] = useState('');

    const [proximaParcelaId, setProximaParcelaId] = useState<string | null>(null);


    const params = useLocalSearchParams<{ initialTab?: HistoryType, emprestimoId?: string }>();
    const router = useRouter();


    const PAGE_SIZE = 20;

    const fetchHistory = useCallback(async (type: HistoryType, pageNum: number, selectedId : string | null = null) => {
        if (pageNum === 0)
            {
                setIsLoading(true);
                setData([]);
        }
        else
        {
            setIsLoadingMore(true);
        }

        try
        {
            let response : any;

            switch (type) {
                case 'emprestimos':
                    response = await getHistoricoEmprestimos(pageNum, PAGE_SIZE);
                    break;
                case 'parcelas_emprestimo':
                    if (selectedId)
                    {
                        response = await getHistoricoParcelas(selectedId, pageNum, PAGE_SIZE);
                    }
                    break;
                case 'propostas':
                    response = await getMinhasPropostas(pageNum, PAGE_SIZE);
                    break;
                case 'historico_proposta':
                    if (selectedId)
                    {
                        response = await getHistoricoPropostas(selectedId, pageNum, PAGE_SIZE);
                    }
                    break;
            
                default:
                    break;
            }
            setData(prevData => (pageNum === 0 ? response.content : [...prevData, ...response.content]));
            setHasMore(!response.last);
            setPage(pageNum);
        }
        catch (error)
        {
            console.error("Erro ao buscar histórico:", error);
        }
        finally
        {
            setIsLoading(false);
            setIsLoadingMore(false);
        }

    }, []);

    const handleLoadMore = () => {
        if (!isLoadingMore && hasMore) {
            fetchHistory(historyType, page + 1, selectedEmprestimoId);
        }
    };

    const resetStateForNewTab = () => {
        setData([]);
        setPage(0);
        setHasMore(true);
        setSelectedEmprestimoId(null);
    };

    const handleHistoryTypeChange = async (type : HistoryType) => {
        setHistoryType(type); 
        setSelectedEmprestimoId(null);

        if (type === 'emprestimos' || type === 'propostas')
        {
            fetchHistory(type, 0, null);
        }
        else if (type ===  'parcelas_emprestimo' || type === 'historico_proposta')
        {
            try
            {
                const emprestimosSummaryResponse = await getHistoricoEmprestimosSummary();
                setEmprestimosSummary(emprestimosSummaryResponse);

                if (emprestimosSummaryResponse.length > 0)
                {
                    setPickerOptions(emprestimosSummaryResponse.map(emprestimo => ({label: `Empréstimo de ${emprestimo.valor} - ${formatDate(emprestimo.dataContratacao)}`, value: emprestimo.id})));
                    setPickerTitle(type === 'parcelas_emprestimo' ? "Selecione um Empréstimo para ver as Parcelas" : "Selecione um Empréstimo para ver as Propostas");
                    setPickerVisible(true);
                }
            }
            catch (error)
            {
                console.error("Erro ao buscar summary de empréstimos:", error);
            }
        }
    }

    const handlePickerSelect = (value : string) => {
        setPickerVisible(false);
        setSelectedEmprestimoId(value);

        fetchHistory(historyType, 0, value);
    }

    const fetchProximaParcelaData = useCallback(async () => {
        try 
        {
            const emprestimos = await getEmprestimosAtivos();
            if (emprestimos.length > 0)
            {
                const proximaParcela = await getProximaParcela(emprestimos[0].id);
                setProximaParcelaId(proximaParcela.id);
            }
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

    useEffect(() => {

        if (params.initialTab && params.emprestimoId)
        {
            const initialTab = params.initialTab;
            const emprestimoId = params.emprestimoId;

            setHistoryType(initialTab);
            setSelectedEmprestimoId(emprestimoId);
            fetchHistory(initialTab, 0, emprestimoId);

            router.setParams({ initialTab: undefined, emprestimoId: undefined });
        }
        else
        {
            fetchHistory('emprestimos', 0, null);
        }

        fetchProximaParcelaData();
    }, [fetchHistory, fetchProximaParcelaData, params.initialTab, params.emprestimoId, router]);

    const renderItem = ({ item } : { item : Emprestimo | Parcela | Proposta | PropostaHistorico}) => {
        switch (historyType) {
            case 'emprestimos':
                return <EmprestimoHistoricoWidget item={item as Emprestimo}></EmprestimoHistoricoWidget>

            case 'propostas':
                return <PropostaHistoricoWidget item={item as Proposta}></PropostaHistoricoWidget>

            case 'parcelas_emprestimo':
                return <ParcelaWidget parcela={item as Parcela} isNextPayment={item.id === proximaParcelaId} ></ParcelaWidget>
            
            case 'historico_proposta':
                return <PropostaHistoricoItemWidget item={item as PropostaHistorico}></PropostaHistoricoItemWidget>
        
            default:
                return null;
        }
    }

    const renderFooter = () => {
        if (!isLoadingMore) return null;
        return <ActivityIndicator style={{ marginVertical: 20 }} size="large" color={Colors.light.primary} />;
    };

    const DropdownButton: React.FC<{ title: string, onPress: () => void }> = ({ title, onPress }) => (
        <TouchableOpacity style={styles.dropdown} onPress={onPress}>
            <Text style={styles.dropdownText}>{title}</Text>
            <Ionicons name="chevron-down" size={20} color={Colors.light.primary} />
        </TouchableOpacity>
    );

    const Tab = ({ title, type }: { title: string, type: HistoryType }) => (
        <TouchableOpacity
            style={[styles.tab, historyType === type && styles.activeTab]}
            onPress={() => handleHistoryTypeChange(type)}
        >
            <Text style={[styles.tabText, historyType === type && styles.activeTabText]}>{title}</Text>
        </TouchableOpacity>
    );

    return (
        <View style={styles.container}>
            <Text style={styles.pageTitle}>Histórico</Text>

            <View style={styles.tabs}>
                <Tab title="Empréstimos" type="emprestimos" />
                <Tab title="Parcelas" type="parcelas_emprestimo" />
                <Tab title="Propostas" type="propostas" />
                <Tab title="H. Propostas" type="historico_proposta" />
            </View>

            {
                (historyType === 'parcelas_emprestimo' || historyType === 'historico_proposta') &&
                <View style={styles.filterContainer}>
                    <DropdownButton 
                        title={selectedEmprestimoId ? `Empréstimo Selecionado` : 'Selecione um Empréstimo'}
                        onPress={() => setPickerVisible(true)}
                    />
                </View>
            }
            {
                isLoading ? (
                    <ActivityIndicator size="large" color={Colors.light.primary} style={{ marginTop: 50 }}/>
                ) : data.length === 0 ? (
                    <Text style={styles.emptyText}>Nenhum registro encontrado.</Text>
                ) : (
                    <FlatList
                        data={data}
                        renderItem={renderItem}
                        keyExtractor={(item) => item.id.toString()}
                        contentContainerStyle={{ paddingHorizontal: 16 }}
                        onEndReached={handleLoadMore}
                        onEndReachedThreshold={0.5}
                        ListFooterComponent={renderFooter}
                    />
                )
            }

            <Modal
                transparent={true}
                visible={pickerVisible}
                animationType="slide"
                onRequestClose={() => setPickerVisible(false)}
            >
                <Pressable style={styles.modalContainer} onPress={() => setPickerVisible(false)}>
                    <View style={styles.modalContent}>
                        <Text style={styles.modalTitle}>{pickerTitle}</Text>
                        <FlatList
                            data={pickerOptions}
                            keyExtractor={(item) => item.value}
                            renderItem={({ item }) => (
                                <TouchableOpacity style={styles.pickerItem} onPress={() => handlePickerSelect(item.value)}>
                                    <Text style={styles.pickerItemText}>{item.label}</Text>
                                </TouchableOpacity>
                            )}
                        />
                        <TouchableOpacity style={styles.closeButton} onPress={() => setPickerVisible(false)}>
                            <Text style={styles.closeButtonText}>Fechar</Text>
                        </TouchableOpacity>
                    </View>
                </Pressable>
            </Modal>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: Colors.light.background,
    },
    pageTitle: {
        fontSize: 28,
        fontWeight: 'bold',
        color: Colors.light.primaryDark,
        textAlign: 'center',
        marginVertical: 20,
    },
    filterContainer: {
        paddingHorizontal: 16,
        marginBottom: 10,
    },
    dropdown: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: 12,
        backgroundColor: '#fff',
        borderRadius: 8,
        borderWidth: 1,
        borderColor: Colors.light.border,
    },
    dropdownText: {
        fontSize: 16,
        fontWeight: '500',
    },
    tabs: {
        flexDirection: 'row',
        justifyContent: 'space-around',
        marginHorizontal: 16,
        marginBottom: 16,
        backgroundColor: '#e9ecef',
        borderRadius: 8,
    },
    tab: {
        flex: 1,
        paddingVertical: 12,
        alignItems: 'center',
        borderRadius: 8,
    },
    activeTab: {
        backgroundColor: Colors.light.primary,
    },
    tabText: {
        fontSize: 14,
        fontWeight: '600',
        color: Colors.light.textSecondary,
    },
    activeTabText: {
        color: '#fff',
    },
    emptyText: {
        textAlign: 'center',
        marginTop: 50,
        fontSize: 16,
        color: Colors.light.textSecondary,
    },
    modalContainer: {
        flex: 1,
        justifyContent: 'flex-end',
        backgroundColor: 'rgba(0,0,0,0.5)',
    },
    modalContent: {
        backgroundColor: 'white',
        borderTopLeftRadius: 20,
        borderTopRightRadius: 20,
        padding: 20,
        maxHeight: '60%',
    },
    modalTitle: {
        fontSize: 20,
        fontWeight: 'bold',
        marginBottom: 20,
        textAlign: 'center'
    },
    pickerItem: {
        paddingVertical: 15,
        borderBottomWidth: 1,
        borderBottomColor: '#eee'
    },
    pickerItemText: {
        fontSize: 16,
    },
     closeButton: {
        marginTop: 20,
        backgroundColor: Colors.light.atrasadoText,
        padding: 15,
        borderRadius: 8,
        alignItems: 'center'
    },
    closeButtonText: {
        color: 'white',
        fontWeight: 'bold'
    }
});

export default HistoryScreen;