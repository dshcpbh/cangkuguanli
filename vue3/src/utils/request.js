// src/axios.js
import axios from 'axios';
import router from "@/router/index.js";

// 创建一个 Axios 实例
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080', // 请求的基础 URL
    timeout: 10000, // 请求超时设置
});

// 添加请求拦截器
axiosInstance.interceptors.request.use(
    (config) => {
        // 设置 Content-Type
        config.headers['Content-Type'] = 'application/json;charset=UTF-8';

        // 如果是登录接口，跳过 token 的设置
        if (config.url !== '/login') {
            const user = JSON.parse(localStorage.getItem('User'));
            const token = user.token;
            if (token) {
                config.headers['token'] = token + '';
            }
        }

        return config;
    },
    (error) => {
        // 请求错误处理
        return Promise.reject(error);
    }
);

axiosInstance.interceptors.response.use(
    (response) => {
        // 响应成功处理
        return response;
    },
    (error) => {
        // 响应错误处理
        if (error.response) {
            // 服务器返回了错误响应
            console.error('服务器返回了错误响应:', error.response.data);
        } else if (error.request) {
            // 请求没有收到响应
            console.error('请求没有收到响应:', error.request);
        } else{}
    }
)


export default axiosInstance;
