import { NextResponse } from 'next/server';
import { serialize } from 'cookie';

export async function POST() {
    const cookie = serialize('authToken', '', {
        httpOnly: true,
        secure: process.env.NODE_ENV !== 'development',
        expires: new Date(0),
        path: '/',
        sameSite: 'strict',
    });

    return NextResponse.json({ message: 'Logout bem-sucedido' }, {
        status: 200,
        headers: { 'Set-Cookie': cookie },
    });
}