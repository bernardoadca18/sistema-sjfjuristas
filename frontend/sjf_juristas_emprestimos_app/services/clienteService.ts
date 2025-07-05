import api from './api';
import { Cliente, ChavePix } from '../types/Cliente';


export const getPerfil = async (): Promise<Cliente> => {
    try {
        const response = await api.get('/cliente/perfil');
        return response.data;
    } catch (error) {
        console.error("Erro ao buscar perfil do cliente:", error);
        throw error;
    }
};

export const getChavesPix = async (): Promise<ChavePix[]> => {
    try {
        const response = await api.get('/cliente/chaves-pix');
        return response.data;
    } catch (error) {
        console.error("Erro ao buscar chaves PIX:", error);
        throw error;
    }
}