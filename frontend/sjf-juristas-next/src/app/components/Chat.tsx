"use client";

import { useState, useEffect, useRef, /*FormEvent*/ } from 'react';
import { conversationSteps, Message, InputType } from '@/app/utils/conversationSteps';
import { theme } from '@/config/theme';

const Chat = () => {
    const [messages, setMessages] = useState<Message[]>([]);
    const [currentStep, setCurrentStep] = useState(0);
    const [inputValue, setInputValue] = useState('');
    const [isBotTyping, setIsBotTyping] = useState(false);
    const [isChatFinished, setIsChatFinished] = useState(false);

    const chatEndRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        chatEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages, isBotTyping]);


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
    }, []);


    const handleSendMessage = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const currentMessageData = conversationSteps[currentStep];

        if (!inputValue.trim() || !currentMessageData) return;

        const userMessage: Message = {
            id: Date.now(),
            text: inputValue,
            sender: 'user',
        };

        setMessages(prev => [...prev, userMessage]);
        setInputValue('');

        const nextStepIndex = currentStep + 1;
        setCurrentStep(nextStepIndex);

        triggerNextBotMessage(nextStepIndex);

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
            
            if (!nextStepItem.type && step < conversationSteps.length - 1) {
                const nextSequentialStepIndex = step + 1;
                setCurrentStep(nextSequentialStepIndex);
                triggerNextBotMessage(nextSequentialStepIndex);
            }
        }, 0);
    }

    const currentInputData = conversationSteps[currentStep];

    return (
        <div className='flex flex-col h-[80vh] w-[90vw] max-w-2xl mx-auto bg-gray-50 rounded-2xl shadow-2xl font-sans'>
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
            {
                !isChatFinished && currentInputData && currentInputData.type && (
                    <div className="p-4 border-t border-gray-200 rounded-b-2xl" 
                    style={{color: theme.colors.text, background: theme.colors.background}}>
                        <form onSubmit={handleSendMessage} className='flex items-center gap-4'>
                            <input 
                                type={currentInputData.type === InputType.Number ? 'number' : 'text'}
                                value={inputValue}
                                onChange={(e) => setInputValue(e.target.value)}
                                placeholder={currentInputData.placeholder || 'Digite sua resposta...'}
                                className="flex-1 w-full px-4 py-2 bg-gray-100 border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-yellow-200 transition"
                                required
                                minLength={currentInputData.minlength}
                                min={currentInputData.minLoan}
                                max={currentInputData.maxLoan}
                             />

                             <button
                                type='submit'
                                className='p-3 rounded-full hover:bg-yellow-100 focus:outline-none focus:ring-2 focus:bg-yellow-100 focus:ring-offset-2 transition-transform duration-150 ease-in-out transform hover:scale-105 disabled:bg-gray-300 disabled:cursor-not-allowed'
                                disabled={isBotTyping || !inputValue}
                             >
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="feather feather-send"><line x1="22" y1="2" x2="11" y2="13"></line><polygon points="22 2 15 22 11 13 2 9 22 2"></polygon></svg>
                             </button>
                        </form>
                    </div>
                )
            }
        </div>
    )


}

export default Chat;