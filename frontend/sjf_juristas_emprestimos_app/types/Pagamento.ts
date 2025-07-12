export interface PagamentoParcela
{
    id: string;
    dataPagamentoEfetivo: string;
    valorPago: number;
    statusPagamentoAplicado: string;
    referenciaParcela: string;
    meioPagamento: string;
}