export interface Proposta 
{
    id: string;
    valorProposta: number;
    nomeCompletoSolicitante: string;
    emailSolicitante: string;
    numeroParcelasSolicitadas : number;
    numeroParcelasOfertadas? : number;
    taxaJurosDiaria : number;
    dataSolicitacao: string;
    statusProposta: string;
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

export interface RespostaCliente
{
    
}