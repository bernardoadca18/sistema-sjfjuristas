import axios from 'axios';

const BACKEND_URL = "http://192.168.100.10"
const api = axios.create({
    baseURL: BACKEND_URL + "/api"
});

export default api;
