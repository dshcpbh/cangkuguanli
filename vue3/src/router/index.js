import {createRouter, createWebHistory} from 'vue-router'
import store from "@/store/index.js";

const routes = [
    {
        path: '/',
        redirect: '/Login'
    },
    {
        path: '/Login',
        name: 'login',
        component: () => import('@/views/Login.vue')
    },
    {
        path: '/Index',
        name: 'index',
        component: () => import('../views/Index.vue'),
        children: [
            {
                path: '/Home',
                name:'home',
                meta: {
                    title: '首页'
                },
                component: () => import('../views/Home.vue')
            },
            // {
            //     path: '/Admin',
            //     name: 'admin',
            //     meta: {
            //         title: '角色管理'
            //     },
            //     component: () => import('../views/admin/AdminMenage.vue')
            // },
            // {
            //     path: '/User',
            //     name: 'user',
            //     meta: {
            //         title: '用户管理'
            //     },
            //     component: () => import('../views/user/UserMenage.vue')
            // },
        ]
    },

]

const router = createRouter({
    history: createWebHistory(),
    routes
})
router.beforeEach((to, from, next) => {
    const publicPages = ['/login']; // 不需要登录的页面
    const authRequired = !publicPages.includes(to.path.toLowerCase());

    const storedUser = localStorage.getItem("User");
    // console.log('Stored User:', storedUser);
    const user = storedUser ? JSON.parse(storedUser) : null;
    const token = user?.token; // 使用可选链操作符防止 null 报错

    if (authRequired && !token) {
        alert("请先登录");
        next('/');
    } else {
        next();
    }
});

export default router
