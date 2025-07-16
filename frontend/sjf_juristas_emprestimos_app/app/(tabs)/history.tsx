import EmprestimoHistoricoWidget from "@/components/ui/historico/EmprestimoHistoricoWidget";
import PropostaHistoricoItemWidget from "@/components/ui/historico/PropostaHistoricoItemWidget";
import PropostaHistoricoWidget from "@/components/ui/historico/PropostaHistoricoWidget";
import ParcelaWidget from "@/components/ui/ParcelaWidget";
import { Colors } from "@/constants/Colors";
import { getEmprestimosAtivos, getProximaParcela } from "@/services/emprestimoService";
import { getHistoricoEmprestimos, getHistoricoEmprestimosSummary, getHistoricoParcelas, getHistoricoPropostas, getMinhasPropostas } from "@/services/historicoService";
import { Emprestimo, EmprestimoSummary, Parcela } from "@/types/Emprestimo";
import { Proposta, PropostaHistorico } from "@/types/Proposta";
import formatDate from "@/utils/formatDate";
import { Ionicons } from "@expo/vector-icons";
import React, { useCallback, useEffect, useState } from "react";
import { StyleSheet, Text, TouchableOpacity } from "react-native";

type HistoryType = 'emprestimos' | 'parcelas_emprestimo' | 'propostas' | 'historico_proposta';

const HistoryScreen : React.FC = () => {
    const [historyType, setHistoryType] = useState<HistoryType>('emprestimos');
    const [data, setData] = useState<Emprestimo[] | Parcela[] | Proposta[] | PropostaHistorico[]>([]);
    const [isLoading, setIsLoading] = useState(false);

    const [emprestimos, setEmprestimos] = useState<Emprestimo[]>([]);
    const [emprestimosSummary, setEmprestimosSummary] = useState<EmprestimoSummary[]>([]);
    const [propostas, setPropostas] = useState<Proposta[]>([]);
    const [parcelas, setParcelas] = useState<Parcela[]>([]);
    const [historicoProposta, setHistoricoProposta] = useState<PropostaHistorico[]>([]);

    const [selectedEmprestimoId, setSelectedEmprestimoId] = useState<string | null>(null);
    const [selectedPropostaId, setSelectedPropostaId] = useState<string | null>(null);
    
    const [pickerVisible, setPickerVisible] = useState(false);
    const [pickerOptions, setPickerOptions] = useState<{ label: string, value: string }[]>([]);
    const [pickerTitle, setPickerTitle] = useState('');

    const [proximaParcelaId, setProximaParcelaId] = useState<string | null>(null);

    const PAGE_SIZE = 20;

    const fetchHistory = useCallback(async (type: HistoryType, selectedId : string | null = null) => {
        setIsLoading(true);
        setData([]);

        try
        {
            let response;

            switch (type) {
                case 'emprestimos':
                    response = await getHistoricoEmprestimos(0, PAGE_SIZE);
                    setData(response.content);
                    break;
                case 'parcelas_emprestimo':
                    if (selectedId)
                    {
                        response = await getHistoricoParcelas(selectedId, 0, PAGE_SIZE);
                        setData(response.content);
                    }
                    break;
                case 'propostas':
                    response = await getMinhasPropostas(0, PAGE_SIZE);
                    setData(response.content);
                    break;
                case 'historico_proposta':
                    if (selectedId)
                    {
                        response = await getHistoricoPropostas(selectedId, 0, PAGE_SIZE);
                        setData(response.content);
                    }
                    break;
            
                default:
                    break;
            }
        }
        catch (error)
        {
            console.error("Erro ao buscar histórico:", error);
        }
        finally
        {
            setIsLoading(false);
        }

    }, []);

    const handleHistoryTypeChange = async (type : HistoryType) => {
        setHistoryType(type); 
        setSelectedEmprestimoId(null);
        setSelectedPropostaId(null);

        if (type === 'emprestimos')
        {
            fetchHistory('emprestimos');
        }
        else if (type ===  'parcelas_emprestimo' || type === 'historico_proposta')
        {
            const emprestimosSummaryResponse = await getHistoricoEmprestimosSummary();
            setEmprestimosSummary(emprestimosSummaryResponse);

            setPickerOptions(emprestimosSummaryResponse.map(emprestimo => ({label: `Empréstimo de ${emprestimo.valor} - ${formatDate(emprestimo.dataContratacao)}`, value: emprestimo.id})));
            setPickerTitle("Selecione um empréstimo");
            setPickerVisible(true);
        }
        else if (type === 'propostas')
        {
            fetchHistory('propostas');
        }
    }

    const handlePickerSelect = (value : string) => {
        setPickerVisible(false);

        if (historyType === 'parcelas_emprestimo')
        {
            setSelectedEmprestimoId(value);
            fetchHistory('parcelas_emprestimo', value);
        }
        else if (historyType === 'historico_proposta')
        {
            setSelectedEmprestimoId(value);
            fetchHistory('historico_proposta', value);
        }
    }

    const fetchProximaParcelaData = useCallback(async () => {
        try 
        {
            const emprestimos = await getEmprestimosAtivos();
            const emprestimoId = emprestimos[0].id;
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

    useEffect(() => {
        fetchHistory('emprestimos');
        fetchProximaParcelaData();
    }, [fetchHistory, fetchProximaParcelaData]);

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
                break;
        }
    }

    const DropdownButton: React.FC<{ title: string, onPress: () => void }> = ({ title, onPress }) => (
        <TouchableOpacity style={styles.dropdown} onPress={onPress}>
            <Text style={styles.dropdownText}>{title}</Text>
            <Ionicons name="chevron-down" size={20} color={Colors.light.primary} />
        </TouchableOpacity>
    );

    return (<></>);
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