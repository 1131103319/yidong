import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 添加请求拦截器，用于调试
axios.interceptors.request.use(config => {
  console.log('发送请求:', {
    url: config.url,
    method: config.method,
    params: config.params,
    data: config.data,
    headers: config.headers
  });
  return config;
}, error => {
  console.error('请求错误:', error);
  return Promise.reject(error);
});

// 添加响应拦截器，用于调试
axios.interceptors.response.use(response => {
  console.log('收到响应:', {
    url: response.config.url,
    status: response.status,
    statusText: response.statusText,
    headers: response.headers,
    data: response.data
  });
  return response;
}, error => {
  console.error('响应错误:', error);
  if (error.response) {
    console.error('错误响应详情:', {
      url: error.config.url,
      status: error.response.status,
      statusText: error.response.statusText,
      headers: error.response.headers,
      data: error.response.data
    });
  }
  return Promise.reject(error);
});

// 配置axios默认值
axios.defaults.baseURL = process.env.VUE_APP_API_URL || ''
axios.defaults.timeout = 10000
axios.defaults.headers.common['Content-Type'] = 'application/json'

// 创建Vue应用实例
const app = createApp(App)

// 全局配置
app.config.globalProperties.$http = axios

// 使用插件并挂载应用
app.use(store)
   .use(router)
   .use(ElementPlus)
   .mount('#app')