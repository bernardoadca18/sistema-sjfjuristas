import api from './api';
import { Page } from '@/types/Page';
import { PagamentoParcela } from '../types/Pagamento';
import { Parcela } from '@/types/Emprestimo';

export const gerarPixParaPagamento = async (parcelaId: string): Promise<Parcela> => {
    const response = await api.post(`/cliente/pagamentos/parcelas/${parcelaId}/gerar-pix`);
    return response.data;
};

export const anexarComprovante = async (parcelaId: string, comprovante: Blob): Promise<void> => {
    const formData = new FormData();
    formData.append('comprovante', comprovante);

    await api.post(`/cliente/pagamentos/parcelas/${parcelaId}/anexar-comprovante`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });
};

export const getPagamentosPorEmprestimo = async (emprestimoId: string, page: number, size: number): Promise<Page<PagamentoParcela>> => {
    const response = await api.get(`/cliente/pagamentos/emprestimo/${emprestimoId}`, {
        params: {
            page, size,
        },
    });
    return response.data;
};