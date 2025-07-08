const formatCurrency = (value : number) => {
    if (value === undefined || value === null)
    {
        return 'R$ -'; 
    }

    return value.toLocaleString('pt-BR', {
        style: 'currency',
        currency: 'BRL',
    });
}

export default formatCurrency;