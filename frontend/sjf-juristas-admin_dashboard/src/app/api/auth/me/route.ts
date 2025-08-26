import { NextResponse } from 'next/server';
import { cookies } from 'next/headers';
import api from '@/lib/api';

export async function GET()
{
    const cookieStore = await cookies();
    const token = cookieStore.get('authToken')?.value;

    if (!token)
    {
        return NextResponse.json({ message: 'Não autorizado' }, { status: 401 });
    }
    
    try
    {
        const response = await api.get('/admin/profile/me', {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        return NextResponse.json(response.data, { status: 200 });
    }
    catch (error)
    {
        return NextResponse.json({ message: 'Sessão inválida ou expirada\nErro: ' +  error}, { status: 401 });
    }
}