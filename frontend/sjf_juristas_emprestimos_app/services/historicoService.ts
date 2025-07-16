import api from './api';
import { Page } from '@/types/Page';
import { Emprestimo, EmprestimoSummary, Parcela } from '@/types/Emprestimo';
import { Proposta, PropostaHistorico } from '@/types/Proposta';

export const getHistoricoEmprestimos = async (page: number, size: number): Promise<Page<Emprestimo>> => {
    const response = await api.get('/cliente/historico/emprestimos', {
        params: { page, size, sort: 'dataContratacao,desc' },
    });
    return response.data;
};

export const getHistoricoParcelas = async (emprestimoId: string, page: number, size: number): Promise<Page<Parcela>> => {
    const response = await api.get(`/cliente/historico/emprestimos/${emprestimoId}/parcelas`, {
        params: { page, size, sort: 'numeroParcela,asc' },
    });
    return response.data;
};

export const getHistoricoPropostas = async (emprestimoId: string, page: number, size: number): Promise<Page<PropostaHistorico>> => {
    const response = await api.get(`/cliente/historico/emprestimos/${emprestimoId}/propostas`, {
        params: { page, size },
    });
    return response.data;
};

export const getMinhasPropostas = async (page: number, size: number): Promise<Page<Proposta>> => {
    const response = await api.get('/cliente/propostas', {
        params: { page, size, sort: 'dataSolicitacao,desc' },
    });
    return response.data;
};

export const getHistoricoEmprestimosSummary = async () : Promise<EmprestimoSummary[]> => {
    const response = await api.get('/cliente/historico/emprestimos/summary');
    return response.data;
}