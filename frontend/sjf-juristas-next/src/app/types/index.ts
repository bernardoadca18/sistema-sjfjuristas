import { ReactNode } from 'react';

// Tipos de Input
export type ChatInputType = 'text' | 'number' | 'email' | 'tel' | 'file' | 'checkbox';

// Interface para um campo de input genérico
export interface ChatInput {
  type: ChatInputType;
  name: string;
  label: string;
  placeholder?: string;
  pattern?: string;
  minlength?: number;
  min?: number;
  max?: number;
  mask?: '000.000.000-00' | '(00) 00000-0000';
}

// Interface para um campo de input de arquivo
export interface ChatFileInput extends ChatInput {
  type: 'file';
  accept: string;
  required: boolean;
}

export type ChatValidationValue = string | number | boolean | Record<string, File | null>;

// Interface para um passo do chat
export interface ChatStep {
  message: string;
  input: ChatInput | ChatFileInput[] | null;
  validation?: (value: ChatValidationValue) => boolean;
  errorMessage?: string;
}

// Interface para uma mensagem no histórico do chat
export interface ChatMessage {
  id: number;
  sender: 'bot' | 'user';
  content: ReactNode;
}

// Interface para os dados do formulário de empréstimo
export interface LoanFormData {
  [key: string]: string | number | boolean | File | null | Record<string, File | null>;
  loanValue: number;
  fullName: string;
  cpf: string;
  email: string;
  whatsapp: string;
  files: {
    rgCnhFront: File | null;
    rgCnhBack: File | null;
    proofOfAddress: File | null;
  };
  termsAccepted: boolean;
}

