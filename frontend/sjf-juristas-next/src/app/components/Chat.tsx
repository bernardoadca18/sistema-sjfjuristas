"use client";

import React, { useState, useEffect, useRef, FormEvent } from 'react';
import { conversationSteps, Message, InputType, IFormData } from '@/app/utils/conversationSteps';
import { theme } from '@/config/theme';
import { PatternFormat } from 'react-number-format';
import axios from 'axios';

const Chat = () => {
    const [messages, setMessages] = useState<Message[]>([]);
    const [currentStep, setCurrentStep] = useState(0);
    const [isBotTyping, setIsBotTyping] = useState(false);
    const [isChatFinished, setIsChatFinished] = useState(false);
    
    const [inputValue, setInputValue] = useState('');
    const [formData, setFormData] = useState<IFormData>({});
    
    const [termsAccepted, setTermsAccepted] = useState(false);
    const [files, setFiles] = useState<FileList | null>(null);

    const [isAutoScrollActive, setIsAutoScrollActive] = useState(false);

    const chatEndRef = useRef<HTMLDivElement>(null);


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


    // F
    useEffect(() => {
        // Este efeito roda sempre que 'isChatFinished' ou 'formData' mudar.
        // O bloco 'if' executa apenas quando o chat é finalizado.
        if (isChatFinished) {
            
            sendProposalToApi(formData);

            /*
            let informacoesDosArquivos = "Nenhum arquivo enviado.";
            if (formData.documentUpload && formData.documentUpload.length > 0) {
                
                informacoesDosArquivos = Array.from(formData.documentUpload).map(file => 
                    `\n - Nome: ${file.name}, Tamanho: ${(file.size / 1024).toFixed(2)} KB, Tipo: ${file.type}`
                ).join('');
            }
            
            const dadosFinais = {
                valorDesejado: formData.loanValue,
                nomeCompleto: formData.fullName,
                cpf: formData.cpf,
                email: formData.email,
                whatsapp: formData.whatsapp,
                termosAceitos: formData.termsAcceptance,
                arquivos: informacoesDosArquivos,
                objetoFileListOriginal: formData.documentUpload
            };
            */
            // console.log(dadosFinais);
        }
    }, [isChatFinished, formData]);
    ///

    const sendProposalToApi = async (data: IFormData) => {
        const payload = {
            valorSolicitado: data.loanValue,
            nomeCompleto: data.fullName,
            cpf: data.cpf,
            email: data.email,
            whatsapp: data.whatsapp,
            termosAceitos: data.termsAcceptance,
        }

        console.log("Enviando para a API o seguinte payload:", payload);

        try
        {
            const response = await axios.post('http://localhost:8080/api/propostas', payload);
            console.log('Proposta criada com sucesso! Resposta da API:', response.data);
        }
        catch (error)
        {
            console.error("Ocorreu um erro ao enviar a proposta:");

            if (axios.isAxiosError(error) && error.response) {
                console.error('Status do erro:', error.response.status);
                console.error('Detalhes do erro:', error.response.data);
            } else if (axios.isAxiosError(error) && error.request) {
                console.error('Erro de rede ou CORS. Nenhuma resposta recebida.');
            } else {
               console.error('Erro inesperado:', error);
            }
        }
    }

    ///

    const proceedToNextStep = (userResponseText : string, cleanValue: string | number | boolean | FileList | null) => {
        const currentMessageData = conversationSteps[currentStep];
        const messageIdentifier = currentMessageData.message_identifier;

        if (messageIdentifier) {
            let displayText = userResponseText;

            if (currentMessageData.mask) {
                displayText = userResponseText; 
            }
            setFormData(prevData => ({ ...prevData, [messageIdentifier]: cleanValue }));
            const userMessage: Message = { id: Date.now(), text: displayText, sender: 'user' };
            setMessages(prev => [...prev, userMessage]);
        } else {
            const userMessage: Message = { id: Date.now(), text: userResponseText, sender: 'user' };
            setMessages(prev => [...prev, userMessage]);
        }

        setInputValue('');
        setFiles(null);
        setTermsAccepted(false);
        const nextStepIndex = currentStep + 1;
        setCurrentStep(nextStepIndex);
        triggerNextBotMessage(nextStepIndex);
    }


    const handleSendMessage = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!inputValue.trim()) return;

        const currentMessageData = conversationSteps[currentStep];
        let valueToSave : string | number = inputValue; 
        if (!currentMessageData.mask && currentMessageData.type === InputType.Number) {
            valueToSave = parseFloat(inputValue.replace(/\D/g, '')) || 0;
        }

        proceedToNextStep(inputValue, valueToSave);
    }

    const handleFileUpload = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (!files || files.length === 0)
        {
            alert("Por favor, selecione pelo menos um arquivo.");
            return;
        }

        //TODO: Upload dos arquivos pro servidor

        const fileNames = Array.from(files).map(f => f.name).join(', ');
        proceedToNextStep(`Arquivos enviados: ${fileNames}`, files);
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

    const currentInputData = conversationSteps[currentStep];

    //

    const renderInputArea = () => {
        if (isChatFinished || !currentInputData || !currentInputData.type) return null;

        const shouldAutoFocus = messages.length > 1;

        switch (currentInputData.type) {
            case InputType.Text:
            case InputType.Number:
                return (
                    <form onSubmit={handleSendMessage} className='flex items-center gap-4'>
                        {currentInputData.mask ? (
                            <PatternFormat
                                id="chat-input" // Adicionando um ID para referência
                                key={currentInputData.id}
                                format={currentInputData.mask.replace(/0/g, '#')}
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
                                type={currentInputData.type === InputType.Number ? 'number' : 'text'}
                                value={inputValue}
                                onChange={(e) => setInputValue(e.target.value)}
                                className="flex-1 w-full px-4 py-2 bg-gray-100 border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-yellow-200 transition"
                                placeholder={currentInputData.placeholder || 'Digite sua resposta...'}
                                required
                                autoFocus = {shouldAutoFocus}
                                minLength={currentInputData.minlength}
                                min={currentInputData.minLoan} max={currentInputData.maxLoan}
                            />
                        )}
                        <button type='submit' className='p-3 rounded-full hover:bg-yellow-100 focus:outline-none focus:ring-2 focus:bg-yellow-100 focus:ring-offset-2 transition-transform duration-150 ease-in-out transform hover:scale-105 disabled:bg-gray-300 disabled:cursor-not-allowed' disabled={isBotTyping || !inputValue.trim()}>
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><line x1="22" y1="2" x2="11" y2="13"></line><polygon points="22 2 15 22 11 13 2 9 22 2"></polygon></svg>
                        </button>
                    </form>
                );
            
            // LÓGICA DO FILE UPLOAD RESTAURADA
            case InputType.FileUpload:
                return (
                    <form onSubmit={handleFileUpload} className='flex flex-col sm:flex-row items-center gap-4'>
                        <label className="relative cursor-pointer bg-gray-100 border border-gray-300 rounded-full px-4 py-2 text-center w-full sm:w-auto">
                            <span>{files ? `${files.length} arquivo(s)` : currentInputData.label || 'Selecionar Arquivos'}</span>
                            <input 
                                type="file" 
                                multiple 
                                className="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
                                onChange={(e) => setFiles(e.target.files)}
                            />
                        </label>
                         <button type='submit' className='w-full sm:w-auto px-6 py-2 rounded-full bg-yellow-400 text-black font-semibold hover:bg-yellow-500 focus:outline-none focus:ring-2 focus:ring-yellow-300 disabled:bg-gray-300 disabled:cursor-not-allowed' disabled={isBotTyping || !files || files.length === 0}>
                            Confirmar Envio
                         </button>
                    </form>
                );
            
            // LÓGICA DO CHECKBOX RESTAURADA
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