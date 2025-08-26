import { NextResponse } from 'next/server';
import api from '@/lib/api';
import { serialize } from 'cookie';
import { AxiosError } from 'axios';

export async function POST(request: Request) {
    try
    {
        const body = await request.json();
        const { login, senha } = body;

        const response = await api.post('/admin/auth/login', { login, senha });

        const { token, adminId, nomeAdmin } = response.data;

        const cookie = serialize('authToken', token, {
            httpOnly: true,
            secure: process.env.NODE_ENV !== 'development',
            maxAge: 60 * 60 * 24,
            path: '/',
            sameSite: 'strict',
        });
        
        return NextResponse.json(
            { adminId, nomeAdmin },
            {
                status: 200,
                headers: { 'Set-Cookie': cookie },
            }
        );
    } 
    catch (error : unknown)
    {
        if (error instanceof AxiosError) 
            {
            const status = error.response?.status || 500;
            const message = error.response?.data?.message || 'Erro de comunicação com a API.';
            return NextResponse.json({ message }, { status });
        }

        return NextResponse.json(
            { message: 'Ocorreu um erro inesperado.' },
            { status: 500 }
        );
    }

}