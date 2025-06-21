<template>
<div style="display: flex;line-height: 60px;">
  <div style="line-height: 70px;cursor: pointer;">
    <el-icon style="font-size: 20px" @click="collapse" v-if="!collapseStatus"><Fold /></el-icon>
    <el-icon style="font-size: 20px" @click="collapse" v-else><Expand /></el-icon>
  </div>
  <div style="font-size: 34px;text-align: center;flex: 1">
      <span>
        欢迎来到后台管理系统
      </span>
  </div>
  <div class="toolbar">
    <el-dropdown>
      <div style="width: 150px;height: 60px;display: flex;align-items: center;justify-content: center;cursor: pointer;">
        <div style="margin-top: 1px">
          <el-avatar
              :src="User.txpath"
              @error="errorHandler"
          >
            <img
                src="http://localhost:8080/files/download/OIP.jpg"
            />
          </el-avatar>
        </div>
        <div style="margin-left: 10px">
          <span style="font-size: 16px;color: #303133">{{User.name}}</span>
        </div>
        <div style="margin-left: 10px">
        <el-icon style="margin-right: 8px; margin-top: 1px">
          <Setting />
        </el-icon>
        </div>
      </div>

      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click="toUser">个人中心</el-dropdown-item>
          <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</div>
</template>

<script setup>

import {ref} from "vue";
import router from "@/router/index.js";
import store from "@/store/index.js";

const collapseStatus = ref(false)
const emit = defineEmits(['doCollapse'])
const User =  ref(JSON.parse(localStorage.getItem("User")))
const errorHandler = () => true


function  collapse() {
  collapseStatus.value = !collapseStatus.value
  //提交数据到index
  emit('doCollapse',collapseStatus.value)
}

function logout() {
  localStorage.clear();
  // 手动清除 Vuex 的 menu 数据
  router.push('/Login')
}


</script>

<style scoped>
.toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}
</style>
