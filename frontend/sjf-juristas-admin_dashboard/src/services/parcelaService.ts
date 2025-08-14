import api from "@/lib/api";

const BASE_URL = "/admin/parcelas";

export const fetchComprovantesDaParcela = async (parcelaId: string) => {
    try
    {
        const response = await api.get(`${BASE_URL}/${parcelaId}/comprovantes`);
        return response.data;
    }
    catch (error)
    {
        console.error('Erro ao buscar comprovantes da parcela:', error);
    }
};