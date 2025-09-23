'use client';

import { useState } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { useRouter } from 'next/navigation';
import axios, { AxiosError } from 'axios';

export const LoginForm = () => {
    const [login, setLogin] = useState('');
    const [senha, setSenha] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const auth = useAuth();
    const router = useRouter();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setError('');
        try
        {
            const response = await axios.post('/api/login', { login, senha });
            const { token, ...userData } = response.data;
            auth.login(userData);
            router.push('/dashboard');
        } 
        catch (err: unknown) 
        {
            if (err instanceof AxiosError) 
            {
                setError(err.response?.data?.message || 'Falha ao fazer login. Verifique suas credenciais.');
            } 
            else 
            {
                setError('Ocorreu um erro inesperado.');
            }
        } 
        finally 
        {
            setIsLoading(false);
        }
    };

    return (
        <div className="w-full max-w-md bg-card p-8 rounded-lg shadow-md border border-border">
        <h1 className="text-3xl font-bold text-center text-text mb-6">
            Login do Administrador
        </h1>
        <form onSubmit={handleSubmit}>
            <div className="mb-4">
            <label className="block text-textSecondary text-sm font-bold mb-2" htmlFor="login">
                Usuário
            </label>
            <input
                id="login"
                type="text"
                value={login}
                onChange={(e) => setLogin(e.target.value)}
                className="shadow appearance-none border border-border rounded w-full py-3 px-4 text-text leading-tight focus:outline-none focus:ring-2 focus:ring-primary"
                required
            />
            </div>
            <div className="mb-6">
            <label className="block text-textSecondary text-sm font-bold mb-2" htmlFor="senha">
                Senha
            </label>
            <input
                id="senha"
                type="password"
                value={senha}
                onChange={(e) => setSenha(e.target.value)}
                className="shadow appearance-none border border-border rounded w-full py-3 px-4 text-text leading-tight focus:outline-none focus:ring-2 focus:ring-primary"
                required
            />
            </div>
            {error && <p className="bg-red-100 text-red-700 p-3 rounded mb-4 text-center">{error}</p>}
            <div className="flex items-center justify-center">
            <button
                type="submit"
                disabled={isLoading}
                className="bg-primary hover:bg-primaryDark text-textOnPrimary font-bold py-3 px-6 rounded-lg focus:outline-none focus:shadow-outline transition duration-300 disabled:bg-gray-400"
            >
                {isLoading ? 'Entrando...' : 'Entrar'}
            </button>
            </div>
        </form>
        </div>
    );
};