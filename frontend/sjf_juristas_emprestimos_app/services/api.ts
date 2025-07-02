import axios from 'axios';

const BACKEND_URL_DEV = '';

const api = axios.create({
  baseURL: BACKEND_URL_DEV + '/api'
});

export default api;
