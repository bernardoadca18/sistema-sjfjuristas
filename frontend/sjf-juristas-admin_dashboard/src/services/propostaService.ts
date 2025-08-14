import api from "@/lib/api";
import { CondicoesAprovadasDTO, ContrapropostaDTO } from "@/types";

const BASE_URL = "/admin/propostas";

export const fetchPropostasEmAnalise = async (usuarioId : string = '', paginaAtual : number = 1, tamanhoPagina : number = 10) => {
    try
    {
        const response = await api.get(`${BASE_URL}/em-analise`, {
            params: {
                usuarioId: usuarioId,
                page: paginaAtual,
                size : tamanhoPagina
            }
        });
        return response.data;
    }
    catch (error)
    {
        console.error('Erro ao buscar propostas:', error);
    }
};

export const fetchPropostas = async  (usuarioId : string = '', paginaAtual : number = 1, tamanhoPagina : number = 10) => {
    try
    {
        const response = await api.get(`${BASE_URL}`, {
            params: {
                usuarioId: usuarioId,
                page: paginaAtual,
                size : tamanhoPagina
            }
        });
        return response.data;
    }
    catch (error)
    {
        console.error('Erro ao buscar propostas:', error);
    }
};

export const getHistoricoProposta = async (paginaAtual : number = 1, tamanhoPagina : number = 10, propostaId : string) => {
    try
    {
        const response = await api.get(`${BASE_URL}/${propostaId}/historico`, {
            params: {
                page: paginaAtual,
                size: tamanhoPagina
            }
        });
        return response.data;
    }
    catch (error)
    {
        console.error('Erro ao buscar histórico de propostas:', error);
    }
};

export const negarProposta = async (propostaId : string, motivo : string) => {
    try
    {
        const response = await api.post(`${BASE_URL}/${propostaId}/negar`, motivo);
        return response.data;
    }
    catch (error)
    {
        console.error('Erro ao negar proposta:', error);
    }
};

export const enviarContraproposta = async (propostaId : string, data : ContrapropostaDTO) => {
    try
    {
        const response = await api.post(`${BASE_URL}/${propostaId}/contraproposta`, data);
        return response.data;
    }
    catch (error)
    {
        console.error('Erro ao enviar contraproposta:', error);
    }
};

export const aprovarProposta = async (propostaId : string, condicoes: CondicoesAprovadasDTO) => {
    try
    {
        const response = await api.post(`${BASE_URL}/${propostaId}/aprovar`, condicoes);
        return response.data;
    }
    catch (error)
    {
        console.error('Erro ao aprovar proposta:', error);
    }
}

export const definirJuros = async (propostaId : string, taxaJurosDiaria: number) => {
    try
    {
        const response = await api.put(`${BASE_URL}/${propostaId}/definir-juros`, taxaJurosDiaria);
        return response.data;
    }
    catch (error)
    {
        console.error('Erro ao definir taxa de juros:', error);
    }
};