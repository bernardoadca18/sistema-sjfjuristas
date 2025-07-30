'use client'

import React, { useState, useEffect, Suspense } from 'react';
import { useSearchParams } from 'next/navigation';
import redefinirComToken from '@/services/passwordResetService';
import { theme } from '@/config/theme';

const ResetPasswordComponent : React.FC = () => {
    const searchParams = useSearchParams();

    const [token, setToken] = useState<string | null>(null);
    const [novaSenha, setNovaSenha] = useState<string>('');
    const [confirmarSenha, setConfirmarSenha] = useState<string>('');
    const [mensagem, setMensagem] = useState<string>('');
    const [carregando, setCarregando] = useState<boolean>(true);

    useEffect(() => {
        const tokenFromUrl = searchParams.get('token');

        if (tokenFromUrl) {
            setToken(tokenFromUrl);
            setCarregando(false);
        } else {
            setMensagem('Token inválido ou ausente.');
            setCarregando(false);
        }
    }, [searchParams])

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        setMensagem('');

        if (novaSenha !== confirmarSenha)
        {
            setMensagem('As senhas não coincidem.');
            return;
        }

        if (!token)
        {
            setMensagem('Token inválido ou ausente.');
            return;
        }

        setCarregando(true);

        try
        {
            redefinirComToken(token, novaSenha, confirmarSenha);
            setMensagem('Senha redefinida com sucesso! Você já pode fazer login.');
        }
        catch (error : unknown)
        {
            if (error instanceof Error)
            {
                setMensagem(`Erro ao redefinir senha: ${error.message}`);
            }
            else
            {
                setMensagem('Erro desconhecido ao redefinir senha.');
            }
        }
        finally
        {
            setCarregando(false);
        }
    }

    const getMessageStyle = () => {
        if (!mensagem) return {};
        const isError = mensagem.toLowerCase().includes('erro') || mensagem.toLowerCase().includes('inválido') || mensagem.toLowerCase().includes('coincidem') || mensagem.toLowerCase().includes('caracteres');
        return isError 
            ? { ...styles.message, ...styles.errorMessage }
            : { ...styles.message, ...styles.successMessage };
    };

    const buttonStyle = carregando 
        ? { ...styles.button, ...styles.buttonDisabled }
        : styles.button;

    if (carregando)
    {
        return <div>Verificando link...</div>
    }

    if (!token)
    {
        return (
            <div>
                <h1>Verificando link...</h1>
                <p>Se você não for redirecionado, o link pode ser inválido ou ter expirado.</p>
            </div>
        );
    }

    return (
        <div style={styles.pageContainer}>
            <div style={styles.card}>
                <h1 style={styles.title}>Crie sua nova senha</h1>
                <form onSubmit={handleSubmit} style={styles.form}>
                    <input type="hidden" value={token} />
                    <div style={styles.inputGroup}>
                        <label htmlFor="novaSenha" style={styles.label}>
                            Nova Senha
                        </label>
                        <input 
                            id="novaSenha" 
                            type="password" 
                            value={novaSenha} 
                            onChange={(e) => setNovaSenha(e.target.value)} 
                            style={styles.input}
                            required
                        />
                    </div>
                    <div style={styles.inputGroup}>
                        <label htmlFor="confirmarSenha" style={styles.label}>
                            Confirmar Nova Senha
                        </label>
                        <input 
                            id="confirmarSenha" 
                            type="password" 
                            value={confirmarSenha} 
                            onChange={(e) => setConfirmarSenha(e.target.value)} 
                            style={styles.input}
                            required
                        />
                    </div>
                    <button type="submit" disabled={carregando} style={buttonStyle}>
                        {carregando ? 'Salvando...' : 'Salvar Nova Senha'}
                    </button>
                </form>
                {mensagem && <p style={getMessageStyle()}>{mensagem}</p>}
            </div>
        </div>
    );
};

const ResetPasswordPage = () => {
    const fallbackStyle = {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        minHeight: '100vh',
        backgroundColor: theme.colors.background,
        color: theme.colors.textSecondary,
        fontFamily: 'sans-serif',
        fontSize: '18px',
    };

    return (
        <Suspense fallback={<div style={fallbackStyle}>Carregando...</div>}>
            <ResetPasswordComponent />
        </Suspense>
    )
}

const styles: { [key: string]: React.CSSProperties } = {
    pageContainer: {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        minHeight: '100vh',
        backgroundColor: theme.colors.background,
        padding: theme.spacing.medium,
        fontFamily: 'sans-serif',
    },
    card: {
        backgroundColor: theme.colors.card,
        padding: theme.spacing.large,
        borderRadius: theme.borderRadius,
        boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)',
        width: '100%',
        maxWidth: '450px',
        textAlign: 'center',
    },
    title: {
        color: theme.colors.text,
        marginBottom: theme.spacing.large,
        fontSize: '24px',
    },
    form: {
        display: 'flex',
        flexDirection: 'column',
        gap: theme.spacing.medium,
    },
    inputGroup: {
        display: 'flex',
        flexDirection: 'column',
        textAlign: 'left',
    },
    label: {
        color: theme.colors.textSecondary,
        marginBottom: theme.spacing.small,
        fontWeight: 'bold',
        fontSize: '14px',
    },
    input: {
        padding: theme.spacing.medium,
        border: `1px solid ${theme.colors.border}`,
        borderRadius: theme.borderRadius,
        fontSize: '16px',
        backgroundColor: theme.colors.background,
        color: theme.colors.text,
    },
    button: {
        padding: theme.spacing.medium,
        backgroundColor: theme.colors.primary,
        color: theme.colors.textOnPrimary,
        border: 'none',
        borderRadius: theme.borderRadius,
        fontSize: '16px',
        fontWeight: 'bold',
        cursor: 'pointer',
        transition: 'background-color 0.3s ease',
    },
    buttonDisabled: {
        backgroundColor: theme.colors.border,
        color: theme.colors.textSecondary,
        cursor: 'not-allowed',
    },
    message: {
        marginTop: theme.spacing.medium,
        padding: theme.spacing.small,
        borderRadius: theme.borderRadius,
        fontWeight: '500',
    },
    successMessage: {
        backgroundColor: theme.colors.primaryLight,
        color: theme.colors.primaryDark,
    },
    errorMessage: {
        backgroundColor: '#F8D7DA',
        color: '#721C24',
    },
    loadingText: {
        color: theme.colors.textSecondary,
        fontSize: '18px',
    }
};

export default ResetPasswordPage;