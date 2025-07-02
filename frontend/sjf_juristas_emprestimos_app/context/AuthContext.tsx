import api from '@/services/api';
import { FinalizarCadastroDTO, LoginRequestDTO, PreCadastroCheckDTO } from '@/utils/authtypes';
import AsyncStorage from '@react-native-async-storage/async-storage';
import React, { createContext, useContext, useEffect, useState } from 'react';

interface User {
  nomeUsuario: string;
  token: string;
  usuarioId: number;
}

interface OnboardingData {
  usuarioId: number;
  nomeCompleto: string;
  email: string;
}

interface AuthContextType {
  user: User | null;
  isLoading: boolean;
  onboardingData: OnboardingData | null;
  signIn: (data: LoginRequestDTO) => Promise<{ success: boolean; error?: string }>;
  signOut: () => Promise<void>;
  checkPreCadastro: (data: PreCadastroCheckDTO) => Promise<{ success: boolean; error?: string }>;
  completeRegistration: (data: FinalizarCadastroDTO) => Promise<{ success: boolean; error?: string }>;
}

export const AuthContext = createContext<AuthContextType | null>(null);

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth deve ser usado dentro de um AuthProvider');
  }
  return context;
}

export function AuthProvider({ children }: { children: React.ReactNode }) {
    const [user, setUser] = useState<any>(null);
    const [onboardingData, setOnboardingData] = useState<OnboardingData | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const loadUserFromStorage = async () => {
            try
            {
                const storedUser = await AsyncStorage.getItem('user-session');
                if (storedUser) 
                {
                    const parsedUser = JSON.parse(storedUser);
                    api.defaults.headers.common['Authorization'] = `Bearer ${parsedUser.token}`;
                    setUser(parsedUser);
                }
            }
            catch (e)
            {
                console.error("Falha ao carregar sessão do usuário.", e);
            }
            finally
            {
                setIsLoading(false);
            }
        }

        loadUserFromStorage();
    }, []);

    const handleSetUser = async (newUser: User | null) => {
        if (newUser) 
        {
            api.defaults.headers.common['Authorization'] = `Bearer ${newUser.token}`;
            await AsyncStorage.setItem('user-session', JSON.stringify(newUser));
        } 
        else 
        {
            delete api.defaults.headers.common['Authorization'];
            await AsyncStorage.removeItem('user-session');
        }
        setUser(newUser);
    }

    const signIn = async (data: LoginRequestDTO) => {
        try 
        {
            const response = await api.post('/auth/login', data);
            await handleSetUser(response.data);
            return { success: true };
        } 
        catch (error: any)
        {
            return { success: false, error: error.response?.data || "Email ou senha inválidos." };
        }
    };

    const signOut = async () => {
        await handleSetUser(null);
    }

    const checkPreCadastro = async (data: PreCadastroCheckDTO) => {
        try 
        {
            const response = await api.post('/auth/verificar-pre-cadastro', data);
            setOnboardingData(response.data);
            return { success: true };
        }
        catch (error: any)
        {
            return { success: false, error: error.response?.data || "Erro ao verificar dados." };
        }
    }

    const completeRegistration = async (data: FinalizarCadastroDTO) => {
        try
        {
            const response = await api.put('/auth/finalizar-cadastro', data);
            await handleSetUser(response.data);
            setOnboardingData(null);
            return { success: true };
        }
        catch (error : any)
        {
            return { success: false, error: error.response?.data || "Erro ao finalizar cadastro." };
        }
    }

    return (
    <AuthContext.Provider
      value={{
        user,
        isLoading,
        onboardingData,
        signIn,
        signOut,
        checkPreCadastro,
        completeRegistration,
      }}>
      {children}
    </AuthContext.Provider>
  );
}