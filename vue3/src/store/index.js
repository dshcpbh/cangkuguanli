import { createStore } from 'vuex';
import router from "@/router/index.js";
import createPersistedState from "vuex-persistedstate";//持久化 状态

function addNewRoutes(menuList) {
    // console.log(menuList)
    let routes = router.options.routes;
    // console.log("routes",routes)
    let newRoute = null;

    // console.log("routes",routes)
    routes.forEach(routeItem => {
        if (routeItem.path === '/Index') {
            menuList.forEach(menu => {
                let childRoute = {
                    path: '/' + menu.menuclick,
                    name: menu.menuname,
                    meta: {title: menu.menuname},
                    component: () => import(/* @vite-ignore */'../views/' + menu.menucomponent)
                };
                routeItem.children.push(childRoute);
            });
            newRoute = routeItem;
        }
    })
    if (router.hasRoute('index')) {
        // console.log("新", router.hasRoute('index'))
        router.removeRoute('index');
    }
    if (newRoute) {
        // console.log("新路由", newRoute)
        router.addRoute('/Index', newRoute); // 可能重复添加
        console.log("新路由添加成功", newRoute)
    }
}

const store = createStore({
    state() {
        return {
            menu: [],
        }
    },
    mutations: {
        setMenu(state,menuList) {
            state.menu = menuList
            addNewRoutes(menuList)
        },
        setRouter(state, menuList) {
            addNewRoutes(menuList)
        }
    },
    getters: {
        getMenu(state) {
            return state.menu
        }
    },
    plugins: [createPersistedState()]
})

export default store;
// 持久化 状态
export {
    addNewRoutes
}
