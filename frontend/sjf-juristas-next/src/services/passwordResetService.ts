const redefinirComToken = async (token: string, senha: string, confirmarSenha: string): Promise<void> => {
    
    const response = await fetch('/api/senha/redefinir-com-token',
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                token: token,
                novaSenha: senha,
                confirmarNovaSenha: confirmarSenha,
            }),
        }
    );

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Falha ao redefinir a senha.');
    }
}

export default redefinirComToken;