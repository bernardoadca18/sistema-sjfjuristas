export interface Cliente
{
  id: string;
  nomeCompleto: string;
  cpf: string;
  email: string;
  telefoneWhatsapp: string;
  dataNascimento: string;
  dataCadastro: string;
  emailVerificado: boolean;
}

export interface ChavePix
{
    id: string;
    tipoChavePixNome: string;
    valorChaveMascarado: string;
    ativaParaDesembolso: boolean;
}

export interface TipoChavePix
{
    id: string;
    nomeTipo: string;
    mascara?: string;
}