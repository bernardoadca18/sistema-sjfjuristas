import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';

const BACKEND_URL_DEV = "https://14d94295c372.ngrok-free.app"
const api = axios.create({
    baseURL: BACKEND_URL_DEV + '/api'
});

api.interceptors.request.use(
    async (config) => {
        try 
        {
            const token = await AsyncStorage.getItem('token');

            if (token)
            {
                config.headers.Authorization = `Bearer ${token}`;
                console.log('Token anexado ao header:', config.headers.Authorization);
            }
        }
        catch (e)
        {
            console.error('Erro ao buscar o token do AsyncStorage', e);
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (error.response && error.response.status === 401) 
        {
            console.log('Erro 401: Token inválido ou expirado.');
        }
        return Promise.reject(error);
    }
);


export default api;
