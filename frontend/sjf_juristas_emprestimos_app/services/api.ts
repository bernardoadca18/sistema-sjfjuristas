import axios from 'axios';

const BACKEND_URL_DEV = 'https://4864-187-14-151-101.ngrok-free.app';

const api = axios.create({
  baseURL: BACKEND_URL_DEV + '/api'
});

export default api;
