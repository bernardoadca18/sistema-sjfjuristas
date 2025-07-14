import { getEmprestimosAtivos, getParcelas } from "@/services/emprestimoService";
import { getHistoricoEmprestimos, getHistoricoParcelas, getHistoricoPropostas, getMinhasPropostas } from "@/services/historicoService";
import { Emprestimo, Parcela } from "@/types/Emprestimo";
import { Proposta, PropostaHistorico } from "@/types/Proposta";
import React, { useCallback, useState } from "react";

type HistoryType = 'emprestimos' | 'parcelas_emprestimo' | 'propostas' | 'historico_proposta';

const HistoryScreen : React.FC = () => {
    const [historyType, setHistoryType] = useState<HistoryType>('emprestimos');
    const [data, setData] = useState<Emprestimo[] | Parcela[] | Proposta[] | PropostaHistorico[]>([]);
    const [isLoading, setIsLoading] = useState(false);

    const [emprestimos, setEmprestimos] = useState<Emprestimo[]>([]);
    const [propostas, setPropostas] = useState<Proposta[]>([]);
    const [parcelas, setParcelas] = useState<Parcela[]>([]);
    const [historicoProposta, setHistoricoProposta] = useState<PropostaHistorico[]>([]);

    const [selectedEmprestimoId, setSelectedEmprestimoId] = useState<string | null>(null);
    const [selectedPropostaId, setSelectedPropostaId] = useState<string | null>(null);
    
    const [pickerVisible, setPickerVisible] = useState(false);
    const [pickerOptions, setPickerOptions] = useState<{ label: string, value: string }[]>([]);
    const [pickerTitle, setPickerTitle] = useState('');

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

    return (<></>);
};

export default HistoryScreen;