import { parseISO, format } from 'date-fns';

const formatDate = (dateInput : string | Date | number[] | undefined) => {
    if (!dateInput)
    {
        return 'N/A';
    }

    try
    {
        let date : Date;

        if (dateInput instanceof Date)
        {
            date = dateInput;
        }
        else if (typeof dateInput === 'number') 
        {
            date = new Date(dateInput > 9999999999 ? dateInput : dateInput * 1000);
        }
        else if (typeof dateInput === 'string')
        {
            if (dateInput.includes('-') && dateInput.includes('T'))
            {
                date = parseISO(dateInput);
            }
            else
            {
                const parts = dateInput.split(',').map(Number);
                
                if (parts.length === 3 && !parts.some(isNaN))
                {
                    date = new Date(parts[0], parts[1] - 1, parts[2]);
                }
                else
                {
                    date = new Date(dateInput);
                }
            }
        }
        else if (Array.isArray(dateInput) && dateInput.length === 3)
        {
            date = new Date(dateInput[0], dateInput[1] - 1, dateInput[2]);
        }
        else
        {
            throw new Error(`Formato de data não suportado: ${JSON.stringify(dateInput)}`);
        }

        if (isNaN(date.getTime()))
        {
            throw new Error('Data inválida após a conversão');
        }

        return format(date, 'dd/MM/yyyy');
    }
    catch (error)
    {
        console.error('Erro ao formatar data:', error);
        return String(dateInput);
    }
}

export default formatDate;