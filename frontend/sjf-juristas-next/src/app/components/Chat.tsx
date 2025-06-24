"use client";

import React, { useState, useEffect, useRef, FormEvent } from 'react';
import { conversationSteps, Message, InputType, IFormData } from '@/app/utils/conversationSteps';
import { theme } from '@/config/theme';
import { PatternFormat, NumericFormat } from 'react-number-format';
import axios from 'axios';

interface Occupation
{
    id: string;
    nome: string;
}

const Chat = () => {
    const [messages, setMessages] = useState<Message[]>([]);
    const [currentStep, setCurrentStep] = useState(0);
    const [isBotTyping, setIsBotTyping] = useState(false);
    const [isChatFinished, setIsChatFinished] = useState(false);
    const [inputValue, setInputValue] = useState('');
    const [formData, setFormData] = useState<IFormData>({});
    const [numericValue, setNumericValue] = useState<number | undefined>();
    const [termsAccepted, setTermsAccepted] = useState(false);
    const [file, setFile] = useState<File | null>(null);
    const [isAutoScrollActive, setIsAutoScrollActive] = useState(false);
    const chatEndRef = useRef<HTMLDivElement>(null);
    const [occupations, setOccupations] = useState<Occupation[]>([]);
    const [selectedOccupation, setSelectedOccupation] = useState('');
    const [otherOccupation, setOtherOccupation] = useState('');


    const fetchOccupations = async () => {
        try
        {
            const response = await axios.get('/api/ocupacoes');

            const sortedOccupations = response.data.sort((a: Occupation, b: Occupation) => {
                if (a.nome === 'Outros') return -1;
                if (b.nome === 'Outros') return 1;
                return a.nome.localeCompare(b.nome);
            });
            setOccupations(sortedOccupations);
        }
        catch (error)
        {
            console.error("Erro ao buscar ocupações:", error);
        }
    };

    useEffect(() => {
        const currentMessageData = conversationSteps[currentStep];
        if (currentMessageData && currentMessageData.type === InputType.Select) {
            fetchOccupations();
        }
    }, [currentStep]);

    useEffect(() => {
        if (isAutoScrollActive) {
            chatEndRef.current?.scrollIntoView({ behavior: 'smooth' });
        }
    }, [messages, isBotTyping, isAutoScrollActive]);

    useEffect(() => {
        if (isAutoScrollActive) return;

        const userHasReplied = messages.some(msg => msg.sender === 'user');
        if (userHasReplied) {
            setIsAutoScrollActive(true);
        }
    }, [messages, isAutoScrollActive]);


    useEffect(() => {
        if (currentStep === 0)
        {
            setIsBotTyping(true);
            setTimeout(() => {
                const firstMessage = conversationSteps[0];
                setMessages([firstMessage]);
                setIsBotTyping(false);
            }, 100);
        }
    }, [currentStep]);


    useEffect(() => {
        if (isChatFinished) {            
            handleFinalSubmission(formData);
        }
    }, [isChatFinished, formData]);


    const handleFinalSubmission = async (data: IFormData) => {
        const proposalPayload = {
            valorSolicitado: data.loanValue,
            nomeCompleto: data.fullName,
            cpf: data.cpf,
            email: data.email,
            whatsapp: data.whatsapp,
            termosAceitos: data.termsAcceptance,
            dataNascimento: data.dateOfBirth?.replace(/\D/g, ''), 
            numParcelasPreferido: data.installments,
            remuneracaoMensal: data.monthlyIncome,
            ocupacaoId: data.occupationId,
            outraOcupacao: data.otherOccupation,
        }

        console.log("Enviando para a API o seguinte payload:", proposalPayload);

        try
        {
            const proposalResponse = await axios.post(`/api/propostas`, proposalPayload);
            const proposalId = proposalResponse.data.id;

            console.log('Proposta criada com sucesso! ID:', proposalId);
            
            if (proposalId)
            {
                const fileFormData = new FormData();
                
                if (data.docFrente) fileFormData.append('doc_frente', data.docFrente);
                if (data.docVerso) fileFormData.append('doc_verso', data.docVerso);
                if (data.comprovanteResidencia) fileFormData.append('comprovante_residencia', data.comprovanteResidencia);
                if (data.comprovanteRenda) fileFormData.append('comprovante_renda', data.comprovanteRenda);
                if (data.selfie) fileFormData.append('selfie', data.selfie);

                if (fileFormData.entries().next().value)
                {
                    console.log(`Enviando arquivos para a proposta ID: ${proposalId}`);
                    const fileResponse = await axios.post(
                        `/api/propostas/${proposalId}/documentos`,
                        fileFormData,
                        { headers: { 'Content-Type': 'multipart/form-data' } }
                    );
                    console.log('Arquivos enviados com sucesso!', fileResponse.data);
                }
            }
        }
        catch (error)
        {
            console.error("Ocorreu um erro ao enviar a proposta:", error);
            if (axios.isAxiosError(error)) {
                 console.error('Detalhes do erro:', error.response?.data || error.message);
            }
        }
    }

    const proceedToNextStep = (userResponseText : string, cleanValue: string | number | boolean | File | null | Record<string, string | undefined>) => {
        const currentMessageData = conversationSteps[currentStep];
        const messageIdentifier = currentMessageData.message_identifier;

        if (messageIdentifier)
        {
            if (typeof cleanValue === 'object' && cleanValue !== null && !(cleanValue instanceof File))
            {
                setFormData(prevData => ({ ...prevData, ...cleanValue }));
            }
            else
            {
                setFormData(prevData => ({ ...prevData, [messageIdentifier]: cleanValue }));
            }
    
            const userMessage: Message = { id: Date.now(), text: userResponseText, sender: 'user' };
            setMessages(prev => [...prev, userMessage]);
        }
        else
        {
            const userMessage: Message = { id: Date.now(), text: userResponseText, sender: 'user' };
            setMessages(prev => [...prev, userMessage]);
        }

        setInputValue('');
        setNumericValue(undefined);
        setFile(null);
        setTermsAccepted(false);
        setSelectedOccupation('');
        setOtherOccupation('');

        const nextStepIndex = currentStep + 1;
        setCurrentStep(nextStepIndex);
        triggerNextBotMessage(nextStepIndex);
    }

    const handleSelectSubmit = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const occupation = occupations.find(o => o.id === selectedOccupation);
        if (!occupation) return;

        if (occupation.nome === 'Outros' && !otherOccupation.trim())
        {
            alert('Por favor, especifique sua ocupação.');
            return;
        }

        setFormData(prevData => ({
            ...prevData,
            occupationId: occupation.id,
            otherOccupation: occupation.nome === 'Outros' ? otherOccupation : undefined,
        }));
        
        const displayText = occupation.nome === 'Outros' ? `Outros: ${otherOccupation}` : occupation.nome;
        proceedToNextStep(displayText, { occupationId: occupation.id, otherOccupation: otherOccupation });
        
        // Limpa os estados
        setSelectedOccupation('');
        setOtherOccupation('');
    };

    const handleSendMessage = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const currentMessageData = conversationSteps[currentStep];

        if (currentMessageData.type === InputType.Number)
        {
            if (!numericValue) return;
            proceedToNextStep(inputValue, numericValue);
        }
        else
        {
            if (!inputValue.trim()) return;
            proceedToNextStep(inputValue, inputValue);
        }
    }

    const handleFileUpload = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const currentMessageData = conversationSteps[currentStep];
        const isOptional = currentMessageData.label?.includes("(Opcional)");

        if (!file && !isOptional)
        {
            alert("Por favor, selecione um arquivo.");
            return;
        }
        const responseText = file ? `Arquivo enviado: ${file.name}` : "Pular etapa opcional";
        proceedToNextStep(responseText, file);
    }

    const handleTermsAcceptance = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        proceedToNextStep("Li e aceito os termos.", true);
    }

    const triggerNextBotMessage = (step: number) => {
        const nextStepItem = conversationSteps[step];

        if (!nextStepItem) 
        {
            setIsChatFinished(true);
            return;
        }

        setIsBotTyping(true);
        setTimeout(() => {
            setMessages(prev => [...prev, nextStepItem]);
            setIsBotTyping(false);
            
            if (!nextStepItem.type) {
                const nextSequentialStepIndex = step + 1;
                setCurrentStep(nextSequentialStepIndex);
                triggerNextBotMessage(nextSequentialStepIndex);
            }
        }, 0);
    }

    const renderInputArea = () => {
        if (isChatFinished || !conversationSteps[currentStep] || !conversationSteps[currentStep].type) return null;

        const currentInputData = conversationSteps[currentStep];
        const shouldAutoFocus = messages.length > 1;

        switch (currentInputData.type) {
            
            case InputType.Number:
                const isCurrencyVal = (currentInputData.message_identifier === 'loanValue' || currentInputData.message_identifier === 'monthlyIncome');
                let correctComponent = null;

                if (isCurrencyVal)
                {
                    correctComponent = (
                        <NumericFormat
                            key={currentInputData.id}
                            value={inputValue}
                            onValueChange={(values) => {
                                setInputValue(values.formattedValue); // Para exibir no input
                                setNumericValue(values.floatValue);   // Para enviar ao backend
                            }}
                            prefix='R$ '
                            thousandSeparator='.'
                            decimalSeparator=','
                            decimalScale={2}
                            fixedDecimalScale
                            allowNegative={false}
                            className="flex-1 w-full px-4 py-2 bg-gray-100 border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-yellow-200 transition"
                            placeholder={currentInputData.placeholder || 'Digite o valor...'}
                            required
                            autoFocus={shouldAutoFocus}
                         />
                    );
                }
                else
                {
                    correctComponent = (
                        <NumericFormat
                            key={currentInputData.id}
                            value={inputValue}
                            onValueChange={(values) => {
                                setInputValue(values.formattedValue); // Para exibir no input
                                setNumericValue(values.floatValue);   // Para enviar ao backend
                            }}
                            allowNegative={false}
                            className="flex-1 w-full px-4 py-2 bg-gray-100 border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-yellow-200 transition"
                            placeholder={currentInputData.placeholder || 'Digite o valor...'}
                            required
                            autoFocus={shouldAutoFocus}
                         />
                    );
                }

                return (
                    <form onSubmit={handleSendMessage} className='flex items-center gap-4'>
                         {
                            correctComponent
                         }
                         <button type='submit' className='p-3 rounded-full hover:bg-yellow-100 focus:outline-none focus:ring-2 focus:bg-yellow-100 focus:ring-offset-2 transition-transform duration-150 ease-in-out transform hover:scale-105 disabled:bg-gray-300 disabled:cursor-not-allowed' disabled={isBotTyping || !numericValue}>
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><line x1="22" y1="2" x2="11" y2="13"></line><polygon points="22 2 15 22 11 13 2 9 22 2"></polygon></svg>
                         </button>
                    </form>
                 );

            case InputType.Text:
                return (
                    <form onSubmit={handleSendMessage} className='flex items-center gap-4'>
                        {currentInputData.mask ? (
                            <PatternFormat
                                id="chat-input"
                                key={currentInputData.id}
                                format={currentInputData.mask.replace(/[0]/g, '#')}
                                value={inputValue}
                                onValueChange={(values) => setInputValue(values.value)}
                                type='tel'
                                inputMode='numeric'
                                className="flex-1 w-full px-4 py-2 bg-gray-100 border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-yellow-200 transition"
                                placeholder={currentInputData.placeholder || 'Digite sua resposta...'}
                                required
                                autoFocus = {shouldAutoFocus}
                            />
                        ) : (
                            <input
                                id="chat-input"
                                key={currentInputData.id}
                                type={'text'}
                                value={inputValue}
                                onChange={(e) => setInputValue(e.target.value)}
                                className="flex-1 w-full px-4 py-2 bg-gray-100 border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-yellow-200 transition"
                                placeholder={currentInputData.placeholder || 'Digite sua resposta...'}
                                required
                                autoFocus = {shouldAutoFocus}
                                minLength={currentInputData.minlength}
                            />
                        )}
                        <button type='submit' className='p-3 rounded-full hover:bg-yellow-100 focus:outline-none focus:ring-2 focus:bg-yellow-100 focus:ring-offset-2 transition-transform duration-150 ease-in-out transform hover:scale-105 disabled:bg-gray-300 disabled:cursor-not-allowed' disabled={isBotTyping || !inputValue.trim()}>
                           <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><line x1="22" y1="2" x2="11" y2="13"></line><polygon points="22 2 15 22 11 13 2 9 22 2"></polygon></svg>
                        </button>
                    </form>
                );

            case InputType.FileUpload:
                return (
                    <form onSubmit={handleFileUpload} className='flex flex-col sm:flex-row items-center gap-4'>
                        <label className="relative cursor-pointer bg-gray-100 border border-gray-300 rounded-full px-4 py-2 text-center w-full sm:w-auto hover:bg-gray-200 transition">
                            <span>{file ? file.name : currentInputData.label || 'Selecionar Arquivo'}</span>
                            <input 
                                type="file" 
                                className="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
                                onChange={(e) => setFile(e.target.files ? e.target.files[0] : null)}
                            />
                        </label>
                         <button type='submit' className='w-full sm:w-auto px-6 py-2 rounded-full bg-yellow-400 text-black font-semibold hover:bg-yellow-500 focus:outline-none focus:ring-2 focus:ring-yellow-300 disabled:bg-gray-300 disabled:cursor-not-allowed' disabled={isBotTyping || (!file && !currentInputData.label?.includes("(Opcional)"))}>
                            Confirmar Envio
                         </button>
                         {currentInputData.label?.includes("(Opcional)") && !file && (
                             <button type='button' onClick={() => proceedToNextStep("Pular etapa opcional", null)} className='w-full sm:w-auto px-6 py-2 rounded-full bg-gray-200 text-black font-semibold hover:bg-gray-300'>
                                Pular
                             </button>
                         )}
                    </form>
                );

            case InputType.Checkbox:
                return (
                    <form onSubmit={handleTermsAcceptance} className="flex flex-col sm:flex-row items-center gap-4 p-2">
                        <label className="flex items-center gap-2 cursor-pointer flex-1">
                            <input 
                                type="checkbox" 
                                checked={termsAccepted}
                                onChange={(e) => setTermsAccepted(e.target.checked)}
                                className="h-5 w-5 rounded border-gray-300 text-yellow-500 focus:ring-yellow-400"
                            />
                            <span className="text-sm">{currentInputData.label}</span>
                        </label>
                        <button type='submit' className='w-full sm:w-auto px-6 py-2 rounded-full bg-yellow-400 text-black font-semibold hover:bg-yellow-500 focus:outline-none focus:ring-2 focus:ring-yellow-300 disabled:bg-gray-300 disabled:cursor-not-allowed' disabled={isBotTyping || !termsAccepted}>
                            Confirmar
                        </button>
                    </form>
                );

            case InputType.Select:
                return (
                    <form onSubmit={handleSelectSubmit} className='flex flex-col items-stretch gap-4'>
                        <select
                            value={selectedOccupation}
                            onChange={(e) => setSelectedOccupation(e.target.value)}
                            className="w-full px-4 py-3 bg-gray-100 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-yellow-200 transition"
                            required
                        >
                            <option value="" disabled>{currentInputData.label || 'Selecione uma opção'}</option>
                            {occupations.map(occ => (
                                <option key={occ.id} value={occ.id}>{occ.nome}</option>
                            ))}
                        </select>
                        
                        {/* Campo condicional para "Outros" */}
                        {selectedOccupation && occupations.find(o => o.id === selectedOccupation)?.nome === 'Outros' && (
                            <input
                                type="text"
                                value={otherOccupation}
                                onChange={(e) => setOtherOccupation(e.target.value)}
                                placeholder="Por favor, especifique sua ocupação"
                                className="w-full px-4 py-2 bg-gray-100 border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-yellow-200 transition"
                                required
                            />
                        )}

                        <button type='submit' className='px-6 py-2 rounded-full bg-yellow-400 text-black font-semibold hover:bg-yellow-500 disabled:bg-gray-300' disabled={isBotTyping || !selectedOccupation}>
                            Confirmar
                        </button>
                    </form>
                );
            
            default:
                return null;
        }
    }

    return (
        <div id='chat-section' className='flex flex-col h-[80vh] w-[90vw] max-w-2xl mx-auto bg-gray-50 rounded-2xl shadow-2xl font-sans'>
            <div className="p-4 rounded-t-2xl" style={{color: theme.colors.textOnPrimary, background: theme.colors.primary}}>
                <h2 className="text-xl font-bold text-center">Simulação de Empréstimo</h2>
            </div>

            <div className='flex-1 p-6 overflow-y-auto space-y-4'>
                {
                    messages.map ((msg) => (
                        <div key={msg.id} className={`flex items-end gap-2 ${msg.sender === 'bot' ? 'justify-start' : 'justify-end'}`}>
                            {
                                msg.sender === 'bot' && (
                                    <div className="w-8 h-8 rounded-full flex items-center justify-center font-bold flex-shrink-0" style={{color: theme.colors.textOnPrimary, background: theme.colors.primary}}>
                                        B
                                    </div>
                                )
                            }
                            <div
                                className={`max-w-xs md:max-w-md lg:max-w-lg px-4 py-3 rounded-2xl shadow-xl ${
                                msg.sender === 'bot' 
                                ? 'rounded-bl-none' 
                                : 'rounded-br-none'
                                }`}
                                style={ msg.sender === 'bot' ?
                                {
                                    color: theme.colors.text,
                                    background: theme.colors.background
                                }
                                :
                                {
                                    color: theme.colors.text,
                                    background: theme.colors.primaryLight
                                }}
                            >
                                <p className="text-md">{msg.text}</p>
                            </div>
                        </div>
                    ))
                }
                {
                    isBotTyping && (
                        <div className="flex items-end gap-2 justify-start">
                            <div className="w-8 h-8 rounded-full flex items-center justify-center font-bold flex-shrink-0" style={{color: theme.colors.textOnPrimary, background: theme.colors.primary}}>B</div>
                            <div className="px-4 py-3 rounded-2xl bg-gray-200 rounded-bl-none">
                                <div className="flex items-center justify-center space-x-1">
                                    <span className="w-2 h-2 bg-gray-500 rounded-full animate-bounce delay-0"></span>
                                    <span className="w-2 h-2 bg-gray-500 rounded-full animate-bounce delay-150"></span>
                                    <span className="w-2 h-2 bg-gray-500 rounded-full animate-bounce delay-300"></span>
                                </div>
                            </div>
                        </div>
                    )
                }
                <div ref={chatEndRef} />
            </div>
            
            <div className="p-4 border-t border-gray-200 rounded-b-2xl" style={{color: theme.colors.text, background: theme.colors.background}}>
                {renderInputArea()}
            </div>
        </div>
    )


}

export default Chat;