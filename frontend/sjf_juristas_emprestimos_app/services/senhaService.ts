import api from './api';

export const solicitarRedefinicaoSenha = async (email : string): Promise<void> => {
    try
    {
        await api.post(`/senha/solicitar-redefinicao`, {
            "email": email
        });
    } 
    catch (error)
    {
        console.error(`Erro ao enviar solicitação:`, error);
        throw error;
    }
};

export const redefinirSenha = async (senhaAtual: string, novaSenha: string, confirmarNovaSenha : string): Promise<void> => {
    try
    {
        await api.post(`/senha/atualizar-logado`, {
            "senhaAtual": senhaAtual,
            "novaSenha": novaSenha,
            "confirmarNovaSenha": confirmarNovaSenha
        });
    } 
    catch (error)
    {
        console.error(`Erro ao enviar solicitação:`, error);
        throw error;
    }
}