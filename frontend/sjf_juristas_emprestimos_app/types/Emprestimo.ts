export interface Emprestimo
{
    id: string;
    valorContratado: number;
    valorLiberado: number;
    taxaJurosDiariaEfetiva: number;
    numeroTotalParcelas: number;
    valorParcelaDiaria: number;
    dataContratacao: string;
    dataPrimeiroVencimento: string;
    dataUltimoVencimento: string;
    statusEmprestimoNome: string;
    saldoDevedorAtual: number;
    dataInicioCobrancaParcelas: string;
    proximaParcela: Parcela | null;
}
export interface Parcela
{
    id: string;
    numeroParcela: number;
    dataVencimento: string;
    valorTotalParcela: number;
    statusPagamentoParcelaNome: string;
    pixCopiaCola? : string;
    pixQrCodeBase64? : string;
    valorPrincipal: number;
    valorJuros: number;
}

export interface Pagamento
{
    id: string;
    dataPagamentoEfetivo: string;
    valorPago: number;
    statusPagamentoAplicado : string;
    referenciaParcela : string;
    meioPagamento : string;
}