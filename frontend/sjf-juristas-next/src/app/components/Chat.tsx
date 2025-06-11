'use client';

import { useState, useRef, ChangeEvent, RefObject } from 'react';
import { CheckCircle, AlertCircle, UploadCloud, Paperclip } from 'lucide-react';
import Modal from './Modal';
//import { theme } from '@/config/theme';

interface FormDataState {
    loanValue?: number;
    fullName?: string;
    cpf?: string;
    email?: string;
    whatsapp?: string;
    rgCnhFront?: File;
    rgCnhBack?: File;
    proofOfAddress?: File;
    termsAccepted?: boolean;
}

type InputType = 'number' | 'text' | 'email' | 'tel' | 'checkbox';

interface BaseInput {
    name: keyof FormDataState;
    label: string;
}

interface StandardInput extends BaseInput {
    type: InputType;
    placeholder?: string;
    minLength?: number;
    min?: number;
    max?: number;
    mask?: 'cpf' | 'whatsapp';
}

interface FileInput {
    name: 'rgCnhFront' | 'rgCnhBack' | 'proofOfAddress';
    label: string;
}

type ChatStep = {
    message: string;
    errorMessage: string;
    validation: (value: unknown) => boolean;
} & (
    | { input: StandardInput | null }
    | { input: FileInput[] }
);

// Definindo os passos fora do componente para evitar recriação
const chatSteps = [
    {
        message: "Olá! Bem-vindo(a) à SJF Juristas. Para começarmos sua simulação, qual valor de empréstimo você deseja?",
        input: { type: 'number', name: 'loanValue', placeholder: 'Ex: 5000', label: 'Valor desejado (R$):', min: 100, max: 50000 },
        validation: (value: number | undefined) => value && Number(value) >= 100 && Number(value) <= 50000,
        errorMessage: 'Por favor, insira um valor válido entre R$100 e R$50.000.'
    },
    {
        message: 'Ótimo! Agora, por favor, digite seu nome completo:',
        input: { type: 'text', name: 'fullName', placeholder: 'Seu nome completo', label: 'Nome Completo:', minLength: 5 },
        validation: (value: string | undefined) => value && value.length >= 5 && /^[A-Za-zÀ-ú\s]+$/.test(value),
        errorMessage: 'Por favor, insira um nome completo válido (mínimo 5 caracteres).'
    },
    {
        message: 'Certo! Para darmos andamento, qual é o seu CPF?',
        input: { type: 'text', name: 'cpf', placeholder: '000.000.000-00', label: 'CPF:', mask: 'cpf' },
        validation: (value: string | undefined) => value && /^\d{3}\.\d{3}\.\d{3}-\d{2}$/.test(value),
        errorMessage: 'Por favor, insira um CPF válido.'
    },
    {
        message: 'Para entrarmos em contato, qual seu melhor e-mail?',
        input: { type: 'email', name: 'email', placeholder: 'seu.email@exemplo.com', label: 'E-mail:' },
        validation: (value: string | undefined) => value && /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value),
        errorMessage: 'Por favor, insira um e-mail válido.'
    },
    {
        message: 'E por fim, seu número de WhatsApp com DDD, por favor:',
        input: { type: 'tel', name: 'whatsapp', placeholder: '(XX) XXXXX-XXXX', label: 'WhatsApp:', mask: 'whatsapp' },
        validation: (value: string | undefined) => value && /^\(\d{2}\)\s\d{5}-\d{4}$/.test(value),
        errorMessage: 'Por favor, insira um WhatsApp válido com DDD.'
    },
    {
        message: 'Precisamos de algumas fotos dos seus documentos. Por favor, envie uma foto do seu RG/CNH (frente e verso) e um comprovante de residência.',
        input: [
            { name: 'rgCnhFront', label: 'RG/CNH - Frente' },
            { name: 'rgCnhBack', label: 'RG/CNH - Verso' },
            { name: 'proofOfAddress', label: 'Comprovante de Residência' }
        ],
        validation: (files: Partial<Pick<FormDataState, 'rgCnhFront' | 'rgCnhBack' | 'proofOfAddress'>>) => files?.rgCnhFront && files?.rgCnhBack && files?.proofOfAddress,
        errorMessage: 'Por favor, envie todos os três documentos solicitados.'
    },
    {
        message: 'Por favor, leia e aceite nossos Termos de Uso e Política de Privacidade para prosseguir.',
        input: { type: 'checkbox', name: 'termsAccepted' },
        validation: (value: boolean | undefined) => value === true,
        errorMessage: 'Você deve aceitar os Termos de Uso e Política de Privacidade.'
    },
    {
        message: 'Pronto! Sua solicitação foi enviada com sucesso! Em breve entraremos em contato com sua proposta. Obrigado por escolher a SJF Juristas!',
        input: null,
        validation: () => true,
        errorMessage: '',
    }
];

const Chat = () => {
    const [currentStep, setCurrentStep] = useState(0);
    const [formData, setFormData] = useState<FormDataState>({});
    const [error, setError] = useState<string | null>(null);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [modalContent, setModalContent] = useState({ title: '', content: '' });
    const [isModalOpen, setIsModalOpen] = useState(false);

    const fileInputRefs: { [key: string]: RefObject<HTMLInputElement> } = {
        rgCnhFront: useRef<HTMLInputElement>(null),
        rgCnhBack: useRef<HTMLInputElement>(null),
        proofOfAddress: useRef<HTMLInputElement>(null),
    };

    const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
        const { name, value, type, checked } = e.target;
        const key = name as keyof FormDataState;
        const finalValue = type === 'checkbox' ? checked : (type === 'number' ? parseFloat(value) : value);
        setFormData(prev => ({ ...prev, [key]: finalValue }));
    };

    const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
        const { name, files } = e.target;
        if (files && files.length > 0) {
            setFormData(prev => ({ ...prev, [name as keyof FormDataState]: files[0] }));
        }
    };
    
    const applyMask = (value: string, maskType?: 'cpf' | 'whatsapp') => {
        if (!maskType) return value;
        let v = value.replace(/\D/g, '');
        if (maskType === 'cpf') {
            v = v.slice(0, 11);
            v = v.replace(/(\d{3})(\d)/, '$1.$2');
            v = v.replace(/(\d{3})(\d)/, '$1.$2');
            v = v.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
        } else if (maskType === 'whatsapp') {
            v = v.slice(0, 11);
            v = v.replace(/(\d{2})(\d)/, '($1) $2');
            v = v.replace(/(\d{5})(\d)/, '$1-$2');
        }
        return v;
    };
    
    const handleMaskedInputChange = (e: ChangeEvent<HTMLInputElement>) => {
        const step = chatSteps[currentStep];
        if (step.input && !Array.isArray(step.input) && 'mask' in step.input) {
            const maskedValue = applyMask(e.target.value, step.input.mask);
            setFormData(prev => ({ ...prev, [e.target.name as keyof FormDataState]: maskedValue }));
        }
    };

    const handleNextStep = async () => {
        const step = chatSteps[currentStep];
        setError(null);

        let valueToValidate: any;
        if (step.input && Array.isArray(step.input)) {
            valueToValidate = formData;
        } else if (step.input) {
            valueToValidate = formData[step.input.name as keyof FormDataState];
        }

        if (!step.validation(valueToValidate)) {
            setError(step.errorMessage);
            return;
        }

        if (currentStep < chatSteps.length - 1) {
            setIsSubmitting(true);
            
            const submissionData = new FormData();
            Object.entries(formData).forEach(([key, value]) => {
                if (value !== undefined) {
                    submissionData.append(key, value);
                }
            });

            try {
                const response = await fetch('/api/loan', {
                    method: 'POST',
                    body: submissionData,
                });

                if (!response.ok) throw new Error('Falha no envio do formulário.');
                
                setCurrentStep(prev => prev + 1);
            } catch (err) {

                console.error("Falha ao enviar solicitação:", err);
                setError('Houve um erro ao enviar sua solicitação. Tente novamente.');
            } finally {
                setIsSubmitting(false);
            }
        }
    };
    
    const openModal = (type: 'terms' | 'privacy') => {
        setModalContent({ 
            title: type === 'terms' ? 'Termos de Uso' : 'Política de Privacidade', 
            content: `Aqui vai o conteúdo completo ${type === 'terms' ? 'dos seus termos de uso' : 'da sua política de privacidade'}...` 
        });
        setIsModalOpen(true);
    }
    
    const renderInput = () => {
        const step = chatSteps[currentStep];
        if (!step.input) {
             return (
                 <div className="text-center text-green-500">
                     <CheckCircle size={64} className="mx-auto animate-fade-down" />
                     <p className="mt-4 text-lg text-text">{step.message}</p>
                 </div>
             );
        }

        return (
            <div className="w-full animate-fade-up">
                <p className="chat-message bg-background p-4 rounded-custom text-center text-text shadow-md border border-border mb-6">{step.message}</p>
                {Array.isArray(step.input) ? (
                    <div className="space-y-4">
                        {step.input.map(inputConfig => (
                             <div key={inputConfig.name}>
                                 <input
                                     type="file"
                                     name={inputConfig.name}
                                     accept="image/*,.pdf"
                                     ref={fileInputRefs[inputConfig.name]}
                                     onChange={handleFileChange}
                                     className="hidden"
                                 />
                                 <button
                                     onClick={() => fileInputRefs[inputConfig.name].current?.click()}
                                     className="w-full flex items-center justify-center gap-3 text-left p-3 border border-border rounded-custom hover:bg-gray-50 transition-colors"
                                 >
                                    {formData[inputConfig.name] ? <Paperclip className="text-primary" /> : <UploadCloud className="text-textSecondary" />}
                                    <span className={formData[inputConfig.name] ? 'text-text' : 'text-textSecondary'}>
                                         {formData[inputConfig.name] ? (formData[inputConfig.name] as File).name : inputConfig.label}
                                    </span>
                                 </button>
                             </div>
                        ))}
                    </div>
                ) : (
                    <div className="space-y-2">
                         {step.input.type !== 'checkbox' && <label className="font-semibold text-primaryDark" htmlFor={step.input.name}>{step.input.label}</label>}
                         {step.input.type === 'checkbox' ? (
                             <div className="flex items-center gap-3 justify-center pt-4">
                                 <input
                                     type="checkbox"
                                     name={step.input.name}
                                     id={step.input.name}
                                     checked={!!formData[step.input.name]}
                                     onChange={handleInputChange}
                                     className="h-5 w-5 rounded border-gray-300 text-primary focus:ring-primary"
                                 />
                                 <label htmlFor={step.input.name} className="text-sm text-textSecondary">
                                     Eu li e aceito os{' '}
                                     <button type="button" onClick={() => openModal('terms')} className="underline text-primary hover:text-primaryDark">Termos de Uso</button>
                                     {' '}e a{' '}
                                     <button type="button" onClick={() => openModal('privacy')} className="underline text-primary hover:text-primaryDark">Política de Privacidade</button>.
                                 </label>
                             </div>
                         ) : (
                            <input
                                // Removendo 'label' e 'mask' para evitar passar props inválidas ao <input>
                                {...( ({ label, mask, ...rest }) => rest )(step.input) }
                                id={step.input.name}
                                value={formData[step.input.name as keyof FormDataState] as string || ''}
                                onChange={step.input.mask ? handleMaskedInputChange : handleInputChange}
                                className="w-full p-3 border border-border rounded-custom focus:ring-2 focus:ring-primary focus:border-transparent outline-none transition"
                            />
                         )}
                    </div>
                )}
            </div>
        )
    }

    return (
        <section className="py-16 bg-background" id="chat-section">
            <div className="container mx-auto px-5">
                <h3 className="text-4xl font-bold text-center mb-12 text-primary">
                  Comece sua Simulação Agora!
                </h3>
                <div className="max-w-2xl mx-auto bg-card p-6 md:p-8 rounded-lg shadow-md border border-border">
                    <div className="chat-body min-h-[250px] flex flex-col justify-center items-center">
                        {renderInput()}
                        {error && (
                            <div className="mt-4 flex items-center gap-2 text-red-600 animate-fade-up">
                                <AlertCircle size={20}/>
                                <p>{error}</p>
                            </div>
                        )}
                    </div>
                    {currentStep < chatSteps.length - 1 && (
                        <div className="chat-actions mt-6 text-right">
                            <button
                                onClick={handleNextStep}
                                disabled={isSubmitting}
                                className="bg-accent text-textOnAccent px-8 py-3 rounded-full font-bold text-lg transition-transform duration-300 hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed"
                            >
                                {isSubmitting ? 'Enviando...' : 'Próximo'}
                            </button>
                        </div>
                    )}
                </div>
            </div>
            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title={modalContent.title}>
                <p>{modalContent.content}</p>
            </Modal>
        </section>
    );
};

export default Chat;
