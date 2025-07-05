export interface Emprestimo {
    id: string;
    valorContratado: number;
    valorLiberado: number;
    taxaJurosDiariaEfetiva: number;
    numeroTotalParcelas: number;
    valorParcelaDiaria: number;
    dataContratacao: string; // Formato de data ISO 8601
    dataPrimeiroVencimento: string;
    dataUltimoVencimento: string;
    statusEmprestimoNome: string;
    saldoDevedorAtual: number;
    dataInicioCobrancaParcelas: string;
    proximaParcela: Parcela | null;
}
export interface Parcela {
    id: string;
    numeroParcela: number;
    dataVencimento: string; // Formato "YYYY-MM-DD"
    valorTotalParcela: number;
    statusPagamentoParcelaNome: string;
    pixCopiaCola? : string;
    pixQrCodeBase64? : string;
}

export interface Pagamento {
    id: string;
    dataPagamentoEfetivo: string;
    valorPago: number;
    statusPagamentoAplicado : string;
    referenciaParcela : string;
    meioPagamento : string;
}