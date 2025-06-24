export interface IFormData
{
    loanValue?: number;
    fullName?: string;
    cpf?: string;
    dateOfBirth?: string;
    installments?: number;
    occupationId?: string;
    otherOccupation?: string;
    monthlyIncome?: number;
    email?: string;
    whatsapp?: string;
    docFrente?: File;
    docVerso?: File;
    comprovanteResidencia?: File;
    comprovanteRenda?: File;
    selfie?: File;
    termsAcceptance?: boolean;
}

export enum InputType
{
    Text = 1,
    Number = 2,
    Date = 3,
    FileUpload = 4,
    Checkbox = 5,
    Select = 6,
}

export interface Message
{
    id: number;
    text: string;
    sender: 'user' | 'bot';
    placeholder?: string;
    message_identifier?: keyof IFormData;
    label?: string;
    type?: InputType;
    mask?: string;
    pattern?: string;
    min?: number;
    max?: number;
    errorMessage?: string;
    minlength?: number;
    maxlength?: number;
}

export const conversationSteps: Message[] = [
    {
        id: 1,
        text: "Olá! Bem-vindo(a) à SJF Juristas. Para começarmos sua simulação, qual valor de empréstimo você deseja?",
        sender: 'bot',
        placeholder: "R$ 5.000,00",
        message_identifier: "loanValue",
        type: InputType.Number,
        min: 100,
        max: 50000,
        errorMessage: 'Por favor, insira um valor válido entre R$100 e R$50.000.'
    },
    {
        id: 2,
        text: "Entendido. Agora, por favor, digite seu nome completo:",
        sender: 'bot',
        placeholder: "Seu nome completo",
        message_identifier: "fullName",
        type: InputType.Text,
        pattern: '^[A-Za-zÀ-ú\\s]+$',
        errorMessage: 'Por favor, insira um nome completo válido.'
    },
    {
        id: 3,
        text: "Qual a sua data de nascimento?",
        sender: 'bot',
        placeholder: "DD/MM/AAAA",
        message_identifier: "dateOfBirth",
        type: InputType.Text,
        mask: "00/00/0000",
        minlength: 10,
        errorMessage: "Por favor, insira uma data de nascimento válida."
    },
    {
        id: 4,
        text: "Em quantas parcelas diárias você gostaria de pagar? Oferecemos planos de 30 a 180 dias.",
        sender: 'bot',
        placeholder: "Ex: 90",
        message_identifier: "installments",
        type: InputType.Number,
        min: 30,
        max: 180,
        errorMessage: "Por favor, escolha um número de parcelas entre 30 e 180."
    },
    {
        id: 4.1,
        text: 'Legal! Agora, nos diga qual é a sua ocupação principal. Se não encontrar na lista, selecione "Outros".',
        sender: 'bot',
        message_identifier: "occupationId",
        label: "Selecione sua ocupação",
        type: InputType.Select,
    },
    {
        id: 4.2,
        text: 'Por favor, informe sua receita mensal total (incluindo salários, aluguéis, etc.).',
        sender: 'bot',
        placeholder: "R$ 2.000,00",
        message_identifier: "monthlyIncome",
        type: InputType.Number,
        min: 0,
        errorMessage: 'Por favor, insira um valor válido.'
    },
    {
        id: 5,
        text: "Ok. Para darmos andamento, qual é o seu CPF?",
        sender: 'bot',
        placeholder: '000.000.000-00',
        message_identifier: "cpf",
        type: InputType.Text,
        mask: '000.000.000-00',
        minlength: 14,
        errorMessage: 'Por favor, insira um CPF válido.'
    },
    {
        id: 6,
        text: 'Para entrarmos em contato, qual seu melhor e-mail?',
        sender: 'bot',
        placeholder: 'seu.email@exemplo.com',
        message_identifier: "email",
        type: InputType.Text,
        pattern: '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$',
        errorMessage: 'Por favor, insira um e-mail válido.'
    },
    {
        id: 7,
        text: 'E por fim, seu número de WhatsApp com DDD, por favor:',
        sender: 'bot',
        placeholder: '(XX) 9XXXX-XXXX',
        message_identifier: "whatsapp",
        type: InputType.Text,
        mask: '(00) 00000-0000',
        minlength: 15,
        errorMessage: 'Por favor, insira um número de WhatsApp válido.'
    },
    {
        id: 8,
        sender: 'bot',
        text: 'Tudo certo! Agora vamos para a parte dos documentos. Para sua segurança, pedimos um documento por vez. Vamos começar com a FRENTE do seu RG ou CNH.',
    },
    {
        id: 9,
        sender: 'bot',
        text: 'Por favor, envie uma foto da FRENTE do seu documento de identificação.',
        message_identifier: "docFrente",
        label: "RG/CNH (Frente)",
        type: InputType.FileUpload,
    },
    {
        id: 10,
        sender: 'bot',
        text: 'Ótimo! Agora, por favor, envie o VERSO do mesmo documento.',
        message_identifier: "docVerso",
        label: "RG/CNH (Verso)",
        type: InputType.FileUpload,
    },
    {
        id: 11,
        sender: 'bot',
        text: 'Perfeito. Agora, precisamos de um comprovante de residência recente (conta de água, luz, internet, etc.).',
        message_identifier: "comprovanteResidencia",
        label: "Comprovante de Residência",
        type: InputType.FileUpload,
    },
    {
        id: 12,
        sender: 'bot',
        text: 'Estamos quase lá. Envie seu último comprovante de renda (holerite, contracheque, etc.).',
        message_identifier: "comprovanteRenda",
        label: "Comprovante de Renda",
        type: InputType.FileUpload,
    },
    {
        id: 13,
        sender: 'bot',
        text: 'Para finalizar a verificação, envie uma selfie sua segurando o documento de identidade ao lado do rosto. Este passo é opcional, mas acelera sua análise.',
        message_identifier: "selfie",
        label: "Selfie (Opcional)",
        type: InputType.FileUpload,
    },
    {
        id: 14,
        sender: 'bot',
        text: 'Ufa! Documentos recebidos. Por favor, leia e aceite nossos [Termos de Uso](/termos) e [Política de Privacidade](/privacidade) para concluir.',
        message_identifier: "termsAcceptance",
        label: "Eu li e aceito os Termos de Uso e a Política de Privacidade.",
        type: InputType.Checkbox,
    },
    {
        id: 15,
        sender: 'bot',
        text: 'Pronto! Sua solicitação foi enviada com sucesso! Em breve entraremos em contato com sua proposta. Obrigado por escolher a SJF Juristas!',
    }
];