
export interface IFormData {
    loanValue?: number;
    fullName?: string;
    cpf?: string;
    email?: string;
    whatsapp?: string;
    documentUpload?: FileList;
    termsAcceptance?: boolean;
}

export enum InputType {
    Text = 1,
    Number = 2,
    Date = 3,
    FileUpload = 4,
    Checkbox = 5,
}

export interface Message {
    id: number;
    text: string;
    sender: 'user' | 'bot';
    placeholder?: string;
    message_identifier?: keyof IFormData;
    label ?: string;
    type?: InputType;
    pattern?: string;
    mask?: string;
    minlength?: number;
    minLoan?: number;
    maxLoan?: number;
    errorMessage?: string;
}

export const conversationSteps : Message[] = [
    {
        id : 1,
        text: "Olá! Bem-vindo(a) à SJF Juristas. Para começarmos sua simulação, qual valor de empréstimo você deseja ?",
        sender: 'bot',
        placeholder: "Ex: 5000",
        message_identifier: "loanValue",
        label: "Valor desejado (R$):",
        type: InputType.Number,
        pattern: '^[0-9]+$',
        minLoan: 100,
        maxLoan: 50000,
        errorMessage: 'Por favor, insira um valor válido entre R$100 e R$50.000.'
    },
    {
        id : 2,
        text: "Ótimo! Agora, por favor, digite seu nome completo:",
        sender: 'bot',
        placeholder: "Seu nome completo",
        message_identifier: "fullName",
        label: "Nome Completo:",
        type: InputType.Text,
        pattern: '^[A-Za-zÀ-ú\\s]+$',
        minlength: 5,
        errorMessage: 'Por favor, insira um nome completo válido (mínimo 5 caracteres, apenas letras e espaços).'
    },
    {
        id : 3,
        text: "Certo! Para darmos andamento, qual é o seu CPF?",
        sender: 'bot',
        placeholder: '000.000.000-00',
        message_identifier: "cpf",
        label: "CPF:",
        type: InputType.Text,
        mask: '000.000.000-00',
        pattern: '^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})$',
        errorMessage: 'Por favor, insira um CPF válido com 11 dígitos.'
    },
    {
        id : 4,
        text: 'Para entrarmos em contato, qual seu melhor e-mail?',
        sender: 'bot',
        placeholder: 'seu.email@exemplo.com',
        message_identifier: "email",
        label: "E-mail:",
        type: InputType.Text,
        pattern: '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$',
        errorMessage: 'Por favor, insira um e-mail válido.'
    },
    {
        id : 5,
        text: 'E por fim, seu número de WhatsApp com DDD, por favor:',
        sender: 'bot',
        placeholder: '(XX) 9XXXX-XXXX',
        message_identifier: "whatsapp",
        label: "WhatsApp:",
        type: InputType.Text,
        mask: '(00) 00000-0000',
        pattern: '^\\(\\d{2}\\) \\d{5}-\\d{4}$',
        errorMessage: 'Por favor, insira um número de WhatsApp válido no formato (XX) XXXXX-XXXX.'
    },
    {
        id : 6,
        sender: 'bot',
        text: 'Para finalizar, precisamos que você envie alguns documentos (Ex: RG, CPF e Comprovante de Residência).',
        message_identifier: "documentUpload",
        label: "Anexar Documentos",
        type: InputType.FileUpload,
    },
    {
        id: 7,
        sender: 'bot',
        text: 'Por favor, leia e aceite nossos [Termos de Uso](/termos) e [Política de Privacidade](/privacidade) para prosseguir.',
        message_identifier: "termsAcceptance",
        label: "Eu li e aceito os Termos de Uso e a Política de Privacidade.",
        type: InputType.Checkbox,
    },
    {
        id: 8,
        sender: 'bot',
        text: 'Pronto! Sua solicitação foi enviada com sucesso! Em breve entraremos em contato com sua proposta. Obrigado por escolher a SJF Juristas!',
    }
]