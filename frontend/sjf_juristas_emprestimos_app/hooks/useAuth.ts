import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';

const useAuth : unknown = () => {
    const context = useContext(AuthContext);

    if (context === null)
    {
        throw new Error('useAuth must be used within an AuthProvider');
    }

    return context;
}

export default useAuth