const formatarCPF = (cpf : string): string => {
    const cpfDigitos = cpf.replace(/\D/g, '');

    if (cpfDigitos.length !== 11)
    {
        console.warn('CPF inválido. O CPF deve conter 11 dígitos.');
        return cpf;
    }

    return cpfDigitos.replace(
        /(\d{3})(\d{3})(\d{3})(\d{2})/,
        '$1.$2.$3-$4'
    );
};

const formatarTelefone = (telefone: string): string => {
    const telefoneDigitos = telefone.replace(/\D/g, '');

    if (telefoneDigitos.length !== 11)
    {
        console.warn('Número de telefone inválido. O número deve conter 11 dígitos (DDD + número).');
        return telefone;
    }

    return telefoneDigitos.replace(
        /(\d{2})(\d{1})(\d{4})(\d{4})/,
        '($1) $2 $3-$4'
    );
};

const formatarData = (data: string): string => {
    const dataApenasDigitos = data.replace(/\D/g, '');

    if (dataApenasDigitos.length !== 8)
    {
        console.warn('Data inválida. A data deve estar no formato DDMMYYYY.');
        return data;
    }

    return dataApenasDigitos.replace(
        /(\d{2})(\d{2})(\d{4})/,
        '$1/$2/$3'
    );
};

export const formatarInput =  (data: string, messageIdentifier: string): string => {
    switch (messageIdentifier)
    {
        case ("dateOfBirth"):
            return formatarData(data);
        case ("cpf"):
            return formatarCPF(data);
        case("whatsapp"):
            return formatarTelefone(data);
        default:
            return data;
    }
}