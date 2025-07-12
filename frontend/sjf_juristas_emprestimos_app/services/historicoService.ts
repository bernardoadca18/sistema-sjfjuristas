import api from './api';
import { Page } from '@/types/Page';
import { Emprestimo, Parcela } from '@/types/Emprestimo';
import { Proposta } from '@/types/Proposta';

export const getHistoricoEmprestimos = async (page: number, size: number): Promise<Page<Emprestimo>> => {
  const response = await api.get('/cliente/historico/emprestimos', {
    params: { page, size },
  });
  return response.data;
};

export const getHistoricoParcelas = async (emprestimoId: string, page: number, size: number): Promise<Page<Parcela>> => {
  const response = await api.get(`/cliente/historico/emprestimos/${emprestimoId}/parcelas`, {
    params: { page, size },
  });
  return response.data;
};

export const getHistoricoPropostas = async (page: number, size: number): Promise<Page<Proposta>> => {
  const response = await api.get('/cliente/historico/propostas', {
    params: { page, size },
  });
  return response.data;
};