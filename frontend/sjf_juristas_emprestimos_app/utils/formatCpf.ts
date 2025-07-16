export const formatCpf = (cpf: string) => {
    const cleanCpf = cpf.replace(/\D/g, '');

    if (cleanCpf.length !== 11)
    {
        throw new Error("O CPF deve conter exatamente 11 dígitos.");
    }

    return cleanCpf?.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
} 