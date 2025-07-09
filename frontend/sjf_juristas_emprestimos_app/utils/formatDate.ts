import { parseISO, format } from 'date-fns';

const formatDate = (dateString : string | Date | undefined) => {
    if (!dateString)
    {
        return 'N/A';
    }

    try
    {
        const date = typeof dateString === 'string' ? parseISO(dateString) : dateString;
        return format(date, 'dd/MM/yyyy')
    }
    catch (error)
    {
        console.error('Erro ao formatar data:', error);
        return String(dateString);
    }
}

export default formatDate;