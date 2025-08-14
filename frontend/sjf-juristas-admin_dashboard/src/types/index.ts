export interface ContrapropostaDTO {
    valorOfertado: number;
    taxaJurosOfertada: number;
    numParcelasOfertado: number;
    dataDepositoPrevista: string;
    dataInicioPagamentoPrevista: string;
};

export interface CondicoesAprovadasDTO {
    valorContratado: number;
    valorLiberado: number;
    numeroTotalParcelas: number;
    dataPrimeiroVencimento: string;
    taxaJurosDiaria: number;
}