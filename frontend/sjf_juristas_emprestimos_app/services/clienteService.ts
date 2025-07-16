import api from './api';
import { Cliente, ChavePix, TipoChavePix, ClienteUpdateRequest } from '../types/Cliente';

export const getPerfil = async (): Promise<Cliente> => {
    try
    {
        const response = await api.get('/cliente/perfil');
        return response.data;
    } 
    catch (error)
    {
        console.error("Erro ao buscar perfil do cliente:", error);
        throw error;
    }
};

export const getChavesPix = async (): Promise<ChavePix[]> => {
    try
    {
        const response = await api.get('/cliente/chaves-pix');
        return response.data;
    } 
    catch (error)
    {
        console.error("Erro ao buscar chaves PIX:", error);
        throw error;
    }
}

export const getChavePixAtiva = async () : Promise<ChavePix> => {
    try
    {
        const response = await api.get('/cliente/chaves-pix/ativa');
        return response.data;
    } 
    catch (error)
    {
        console.error("Erro ao buscar chaves PIX:", error);
        throw error;
    }
};

export const addChavePix = async (tipoChavePixId: string, valorChave: string, usuarioId?: number): Promise<ChavePix> => {
    try
    {
        if (usuarioId)
        {
            const response = await api.post(`/cliente/chaves-pix/add-to/${usuarioId}`, { tipoChavePixId, valorChave });
            return response.data;
        }
        else
        {
            const response = await api.post('/cliente/chaves-pix', { tipoChavePixId, valorChave });
            return response.data;
        }
    } 
    catch (error)
    {
        console.error("Erro ao adicionar chave PIX:", error);
        throw error;
    }
};

export const setChavePixAtiva = async (chaveId: string): Promise<void> => {
    try
    {
        await api.put(`/cliente/chaves-pix/${chaveId}/ativar`);
    }
    catch (error)
    {
        console.error("Erro ao ativar chave PIX:", error);
        throw error;
    }
}

export const deleteChavePix = async (chaveId: string): Promise<void> => {
    try
    {
        await api.delete(`/cliente/chaves-pix/${chaveId}`);
    } 
    catch (error)
    {
        console.error("Erro ao deletar chave PIX:", error);
        throw error;
    }
};

export const getTiposChavePix = async (): Promise<TipoChavePix[]> => {
    try
    {
        const response = await api.get("/tipo-chave-pix/non-paged");
        return response.data;
    } 
    catch (error)
    {
        console.error("Erro ao buscar tipos de chave PIX:", error);
        throw error;
    }
}

export const updatePerfil = async (data: ClienteUpdateRequest): Promise<void> => {
    try
    {
        await api.put('/cliente/perfil', data);
    }
    catch (error)
    {
        console.error("Erro ao atualizar o perfil do cliente:", error);
        throw error;
    }
};
