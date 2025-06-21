<template>
  <div style="height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            background: #4b72d5">
    <div style="width: 800px;
                height: 500px;display: flex;
                background: white;
                border-radius: 10px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1)">
      <div style="flex: 5">
        <img src="../assets/img1.png" style="width: 100%;height: 100%" alt="logo">
      </div>
      <div style="flex: 4 ;border: 1px solid #ccc;border-radius: 10px">
        <div>
          <n-card>
            <n-tabs
                class="card-tabs"
                default-value="signin"
                size="large"
                animated
                v-model:value="currentTab"
                pane-wrapper-style="margin: 0 -4px"
                pane-style="padding-left: 4px; padding-right: 4px; box-sizing: border-box;"
            >
              <n-tab-pane name="signin" tab="登录">
                <n-form
                    ref="ruleForm"
                    :model="loginForm"
                >
                    <n-form-item-row label="账号" prop="no">
                      <n-input type="text" v-model:value="loginForm.no" placeholder="请输入账号" />
                    </n-form-item-row>
                    <n-form-item-row label="密码" prop="password">
                      <n-input type="password" v-model:value="loginForm.password" placeholder="请输入密码" />
                    </n-form-item-row>
                </n-form>
                <n-button type="primary" @click="Login" block secondary strong>
                  登录
                </n-button>
              </n-tab-pane>
              <n-tab-pane name="signup" tab="注册">
                <template #tab> 注册 </template>
                <n-form
                    ref="registerFormRef"
                    :model="registerForm"
                    v-if="currentTab === 'signup'"
                >
                  <n-form-item-row label="账号" prop="no">
                    <n-input v-model:value="registerForm.no" placeholder="请输入用账号" />
                  </n-form-item-row>
                  <n-form-item-row label="用户名" prop="name">
                    <n-input v-model:value="registerForm.name" placeholder="请输入用户名" />
                  </n-form-item-row>
                  <n-form-item-row label="密码" prop="password">
                    <n-input type="password" v-model:value="registerForm.password" placeholder="请输入密码" />
                  </n-form-item-row>
                  <n-form-item-row label="重复密码" prop="confirmPassword">
                    <n-input type="password" v-model:value="registerForm.confirmPassword" placeholder="请再次输入密码" />
                  </n-form-item-row>
                </n-form>
                <n-button type="primary" block secondary strong @click="Register">
                  注册
                </n-button>
              </n-tab-pane>
            </n-tabs>
          </n-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>

import {ref, reactive, onMounted} from 'vue'
import {ElForm, ElFormItem, ElInput, ElButton, ElMessage} from 'element-plus'
import { useRouter } from 'vue-router'
import axios from "axios";
import router from "@/router/index.js";
import store from "@/store/index.js";

const registerForm = reactive({
  no: '',
  name: '',
  sex: '1',
  age: '0',
  roleId: '1',
  password: '',
  confirmPassword: '',
  txpath:'http://localhost:8080/files/download/OIP.jpg'
})
const currentTab = ref('signin')
const loginForm = reactive({
  no: '',
  password: ''
})


function Login() {
  currentTab.value = 'signin'

  axios.post('/user/login', loginForm).then(res => res.data).then(res => {
    // console.log(res)
    if (res.code === 200) {
      localStorage.setItem('User', JSON.stringify(res.data.user))
      store.commit('setMenu', res.data.menu)
      router.push('/Home')
    } else {
      alert('用户名或密码错误')
    }
  })
}


function clearRegisterForm() {
  registerForm.no = ''
  registerForm.name = ''
  registerForm.sex = '1'
  registerForm.age = '0'
  registerForm.roleId = '0'
  registerForm.password = ''
  registerForm.confirmPassword = ''
}

function Register() {
  axios.post('/user/register', registerForm).then(res => res.data).then(res => {
    console.log(res)
    if (res.code === 200) {
      // console.log(res)
      ElMessage({
        message: '注册成功',
        type: 'success',
      })
      currentTab.value = 'signin'
      loginForm.no = registerForm.no
      loginForm.password = registerForm.password
      clearRegisterForm()
    } else {
      if(res.code === 500){
        alert('账号已存在')
        return
      }
      if(registerForm.no === ''){
        alert('账号不能为空')
        return
      }
      if(registerForm.password !== registerForm.confirmPassword){
        alert('密码不一致')
      }else {
        console.log(registerForm)
        alert('用户名或密码错误')
      }
    }
  })
}

onMounted(() => {

})

</script>

<style scoped>
.card-tabs .n-tabs-nav--bar-type {
  padding-left: 4px;
}
</style>
