export const formatTelefone = (telefone : string) => {
    const cleanTelefone = telefone.replace(/\D/g, '');

    if (cleanTelefone.length === 11)
    {
        return cleanTelefone.replace(/(\d{2})(\d{1})(\d{4})(\d{4})/, '($1) $2 $3-$4');
    }
    else
    {
        return cleanTelefone.replace(/(\d{2})(\d{4})(\d{4})/, '($1) $2-$3');
    }
}