<template>
  <el-container class="layout-container-demo" style="height: 100vh;">
    <el-aside :width="isCollapse? '64px' : '200px'">
       <Aside :isCollapse="isCollapse" />
    </el-aside>
    <el-container>
      <el-header style="text-align: center; font-size: 12px">
        <Header @doCollapse="doCollapse" />
      </el-header>
      <el-main>
<!--        <Main />-->
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>


<script setup>
import Header from "@/views/Header.vue";
import Aside from "@/views/Aside.vue";
import Main from "@/views/Main.vue";
import {ref} from "vue";

 let isCollapse = ref(false);// 侧边栏折叠状态

 // 侧边栏折叠
function doCollapse() {
  isCollapse.value = !isCollapse.value;
}


</script>

<style scoped>
.layout-container-demo .el-header {
  background-color: var(--el-color-primary-light-7);
  color: var(--el-text-color-primary);
}
</style>
