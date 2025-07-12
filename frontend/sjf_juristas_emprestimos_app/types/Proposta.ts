export interface Proposta 
{
    id: string;
    valorSolicitado: number;
    nomeCompletoSolicitante: string;
    emailSolicitante: string;
    dataSolicitacao: string;
    statusProposta: string;
    valorOfertado?: number;
}