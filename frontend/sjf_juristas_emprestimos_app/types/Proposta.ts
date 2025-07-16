export interface Proposta 
{
    id: string;
    valorSolicitado: number;
    nomeCompletoSolicitante: string;
    emailSolicitante: string;
    dataSolicitacao: string;
    statusProposta: string;
    valorOfertado?: number;
    numeroParcelasSolicitadas : number;
    numeroParcelasOfertadas? : number;
    taxaJurosDiaria : number;
}

export interface PropostaHistorico
{
    id: string;
    dataAlteracao: string;
    atorAlteracao: string;
    statusAnterior?: string;
    statusNovo?: string;
    valorAnterior?: number;
    valorNovo?: number;
    numParcelasAnterior?: number;
    numParcelasNovo?: number;
    taxaJurosAnterior?: number;
    taxaJurosNova?: number;
    motivoRecusa?: string;
    observacoes?: string;
}