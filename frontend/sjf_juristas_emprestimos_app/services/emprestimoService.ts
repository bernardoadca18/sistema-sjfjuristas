import api from './api';
import { Emprestimo, Pagamento, Parcela } from '../types/Emprestimo';
import { Page } from '@/types/Page';

export const getEmprestimosAtivos = async (): Promise<Emprestimo[]> => {
    try 
    {
        const response = await api.get('/emprestimos');
        return response.data.content;
    } 
    catch (error)
    {
        console.error("Erro ao buscar empréstimos:", error);
        throw error;
    }
};

export const getEmprestimoDetalhes = async (emprestimoId: string): Promise<Emprestimo> => {
    try 
    {
        const response = await api.get(`/emprestimos/${emprestimoId}`);
        return response.data;
    } 
    catch (error)
    {
        console.error(`Erro ao buscar detalhes do empréstimo ${emprestimoId}:`, error);
        throw error;
    }
};



export const getParcelas = async (emprestimoId: string, page: number = 0, size: number = 100): Promise<Parcela[]> => {
    try 
    {
        const response = await api.get(`/cliente/emprestimos/${emprestimoId}/parcelas`, {
            params: { page, size, sort: 'numeroParcela,asc' }
        });
        return response.data.content;
    } 
    catch (error) 
    {
        console.error(`Erro ao buscar parcelas do empréstimo ${emprestimoId}:`, error);
        throw error;
    }
};

export const getParcelasPaged = async (emprestimoId: string, page: number = 0, size: number = 10): Promise<Page<Parcela>> => {
    try
    {
        const response = await api.get(`/emprestimos/${emprestimoId}/parcelas`, {
            params: { page, size, sort: 'numeroParcela,asc' }
        });
        return response.data;
    }
    catch (error)
    {
        console.error(`Erro ao buscar parcelas do empréstimo ${emprestimoId}:`, error);
        throw error;
    }
};

export const getParcelasForWidget = async (emprestimoId : string) : Promise<Parcela[]> => {
    try
    {
        const response = await api.get(`/emprestimos/${emprestimoId}/widget-parcelas`);
        return response.data;
    }
    catch (error)
    {
        console.error(`Erro ao buscar parcelas do empréstimo ${emprestimoId}:`, error);
        throw error;
    }
}

export const gerarPixParaParcela = async (parcelaId: string): Promise<Parcela> => {
    try 
    {
        const response = await api.post(`/cliente/pagamentos/parcelas/${parcelaId}/gerar-pix`);
        return response.data;
    } 
    catch (error) 
    {
        console.error(`Erro ao gerar PIX para a parcela ${parcelaId}:`, error);
        throw error;
    }
};

export const getProximaParcela = async (emprestimoId: string) : Promise<Parcela> => {
    try 
    {
        const response = await api.get(`/cliente/emprestimos/${emprestimoId}/proxima-parcela`);
        return response.data;
    } 
    catch (error) 
    {
        console.error(`Erro ao buscar a próxima parcela do empréstimo ${emprestimoId}:`, error);
        throw error;
    }
};

export const getPagamentos = async (emprestimoId: string, page?: number, size? : number, sort? : string) : Promise<Pagamento[]> => {
    try 
    {
        const response = await api.get(`/cliente/pagamentos/emprestimo/${emprestimoId}`, {
            params: {
                page: page,
                size: size,
                sort: sort,
            },
        });
        return response.data.content;
    } 
    catch (error) 
    {
        console.error(`Erro ao buscar a próxima parcela do empréstimo ${emprestimoId}:`, error);
        throw error;
    }
}