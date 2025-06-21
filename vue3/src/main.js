import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import axios from 'axios'
import router from './router'
import store, {addNewRoutes} from './store'
import request from "@/utils/request.js";
import naive from 'naive-ui'



const app = createApp(App)
app.config.globalProperties.$http = request;
app.config.globalProperties.$axios = axios
axios.defaults.baseURL = 'http://localhost:8080'

// 在挂载前判断是否有 menu 数据  持久化 状态
const persistedState = JSON.parse(localStorage.getItem('vuex'))
if (persistedState && persistedState.menu && persistedState.menu.length > 0) {
    addNewRoutes(persistedState.menu)
}

app .use(store)
app.use(naive)
app.use(ElementPlus, {
    locale: zhCn,
})
app.use(router)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.mount('#app')
