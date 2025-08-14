import api from "@/lib/api";

export const BASE_URL = "/admin/emprestimos";

export const fetchEmprestimos = async (usuarioId : string = '', paginaAtual : number = 1, tamanhoPagina : number = 10) => {
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
        console.error('Erro ao buscar empréstimos:', error);
    }
};

export const fetchParcelasDoEmprestimo = async (emprestimoId : string, paginaAtual : number = 1, tamanhoPagina : number = 10) => {
    try
    {
        const response = await api.get(`${BASE_URL}/${emprestimoId}/parcelas`, {
            params: {
                page: paginaAtual,
                size: tamanhoPagina
            }
        });
        return response.data;
    }
    catch (error)
    {
        console.error('Erro ao buscar parcelas do empréstimo:', error);
    }
};