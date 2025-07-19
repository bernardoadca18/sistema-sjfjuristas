import { Proposta } from '@/types/Proposta';
import api from './api';

export const responderProposta = async (propostaId: string, acao: 'ACEITAR' | 'RECUSAR', motivoRecusa?: string): Promise<void> => {
    try {
        await api.put(`/cliente/propostas/${propostaId}/responder`, { acao, motivoRecusa });
    } catch (error) {
        console.error(`Erro ao responder proposta ${propostaId}:`, error);
        throw error;
    }
};

export const getPropostaById = async (propostaId: string): Promise<Proposta> => {
    try {
        const response = await api.get(`/cliente/propostas/${propostaId}`);
        return response.data;
    } catch (error) {
        console.error(`Erro ao buscar proposta ${propostaId}:`, error);
        throw error;
    }
}
