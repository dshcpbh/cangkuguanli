<template>
  <div>
    <el-scrollbar style="height: 100vh;">
      <el-menu
           style="height: 100vh;"
          :collapse="props.isCollapse"
           :background-color="isCollapse ? '#C6E2FF' : '#d8d8d2'"
           :collapse-transition="false"
           :default-active="activeIndex"
           router
      >
        <el-menu-item index="/Home">
          <el-icon><HomeFilled /></el-icon>
          <template #title>
            <span>首页</span>
          </template>
        </el-menu-item>
        <el-menu-item :index="'/'+item.menuclick" v-for="(item, i) in menu" :key="i">
          <el-icon>
            <component :is="item.menuicon"></component>
          </el-icon>
          <template #title>
          <span slot="title" style="font-size: 15px;">{{item.menuname}}</span>
          </template>
        </el-menu-item>
<!--        <el-menu-item index="/User">-->
<!--          <el-icon><House /></el-icon>-->
<!--          <span slot="title">首页</span>-->
<!--        </el-menu-item>-->
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
import {computed, onMounted, reactive, ref} from 'vue'
import router from '@/router'
import store from '@/store'
const props = defineProps([ 'isCollapse'])
// 模拟动态路由
// const menu =reactive([
//       {
//         menuclick: "Admin",
//         menuname: "管理员管理",
//         menuicon: "House"
//       },
//       {
//         menuclick: "User",
//         menuname: "用户管理",
//         menuicon: "House"
//       }
const menu = computed(() => store.getters.getMenu)

const activeIndex = ref(router.currentRoute.value.path)


</script>


<style scoped>

</style>
