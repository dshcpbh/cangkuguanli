<template>
  <div>
    <div style="display: flex;align-items: center">
        <div id="welcomeChart" style="width: 100%; height: 60px"></div>
    </div>
    <div class="common-layout">
      <el-container>
        <el-main>
          <el-descriptions
              title="个人信息"
              direction="vertical"
              :column="3"
              :size="size"
              border
          >
            <el-descriptions-item
                :rowspan="4"
                :width="140"
                label="头像"
                align="center"
            >
              <el-image style="width: 150px;
                         height: 150px;
                         border-radius: 50%;
                         "
                        :src="url ? url:'http://localhost:8080/files/download/OIP.jpg'"
                        :preview-src-list="[url ? url:'http://localhost:8080/files/download/OIP.jpg']"
                        :preview-teleported="true"
              />
              <el-button type="text" @click="homehandleEdit">
                <el-icon><edit/></el-icon>
                          编辑信息
              </el-button>
            </el-descriptions-item>
            <el-descriptions-item label="账号" prop="no">{{ user.no }}</el-descriptions-item>
            <el-descriptions-item label="用户名" prop="name">{{ user.name }}</el-descriptions-item>
            <el-descriptions-item label="性别" prop="sex">{{ user.sex === 1 ? "男" : "女" }}</el-descriptions-item>

            <el-descriptions-item label="年龄"  prop="age">{{ user.age }}</el-descriptions-item>
            <el-descriptions-item label="手机号"  prop="phone">{{ user.phone }}</el-descriptions-item>
            <el-descriptions-item label="角色" prop="roleId">
              {{ user.roleId === 0 ? "超级管理员" : user.roleId === 1 ? "管理员" : "普通用户" }}
            </el-descriptions-item>
          </el-descriptions>
        </el-main>
      </el-container>
      <br/><br/>

<!--        可视化数据-->
      <div style="display: flex;">
        <div id="useChart" style="width: 600px;height:400px;"></div>
        <div id="barChart" style="width: 688px;height:400px;"></div>
      </div>
<!--        编辑信息弹窗-->
      <el-dialog v-model="centerDialogVisible" title="编辑信息" width="500" center>
        <el-form  :model="form" label-width="auto" style="max-width: 600px;align-items: center;margin-left: 20px;">
          <el-form-item label="账号" prop="no">
            <el-col :span="20">
              <el-input v-model="form.no"/>
            </el-col>
          </el-form-item>
          <el-form-item label="名字" prop="name">
            <el-col :span="20">
              <el-input v-model="form.name"/>
            </el-col>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-col :span="20">
              <el-input v-model="form.password"/>
            </el-col>
          </el-form-item>
          <el-form-item label="年龄" prop="age">
            <el-col :span="20">
              <el-input v-model="form.age"/>
            </el-col>
          </el-form-item>
          <!--          默认选择男-->
          <el-form-item label="性别" prop="sex">
            <el-radio-group v-model="form.sex" size="medium" fill>
              <el-radio label="1">男</el-radio>
              <el-radio label="0">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-col :span="20">
              <el-input v-model="form.phone"/>
            </el-col>
          </el-form-item>
          <el-form-item label="头像" prop="txpath">
            <el-upload
                v-model:file-list="fileList"
                action="http://localhost:8080/files/upload"
                :show-file-list="true"
                :on-success="handleAvatarSuccess"
                list-type="picture"
            >
              <el-button size="small" type="primary">点击上传</el-button>
            </el-upload>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="centerDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="updateUser">
              保存
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {ref, onMounted, getCurrentInstance, reactive} from "vue";

import type { UploadProps, UploadUserFile } from 'element-plus'
 import {Bottom} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import axios from "axios";
import * as echarts from 'echarts';

const {proxy} = getCurrentInstance()
  const no = ref("");
  const name = ref("");
  const sex = ref('');
  const age = ref('');
  const phone = ref("");
  const roleId = ref(0);
  const size = ref("medium");
 let user = JSON.parse(localStorage.getItem("User"));
 const url = user.txpath;
 const centerDialogVisible = ref(false)

const fileList = ref<UploadUserFile[]>([
  {
    name: '原头像',
    url: user.txpath,
  },
])
// 上传头像
const handleAvatarSuccess = (res) => {
  // console.log("res",res)
  form.txpath = res.data
  // console.log(form.txpath)
  // 提取文件名（从 URL 最后一部分）
}

 const homehandleEdit = () => {
   centerDialogVisible.value = true;
   form = reactive({
     id: user.id,
     no: user.no,
     name: user.name,
     password: user.password,
     age: user.age,
     sex: user.sex+'',
     roleId: user.roleId,
     phone: user.phone,
     token: user.token,
     txpath: user.txpath
   })
 }
 let form = reactive({
     id: '',
     no: '',
     name: '',
     password: '',
     age: '',
     sex: +'',
     roleId: '2',
     phone: '',
     token: '',
     txpath: ''
     })
function updateUser(){
  // centerDialogVisible.value = false;
  proxy.$http.post('/user/update', form).then(res => res.data).then(res => {
    // console.log(res)
    if (res.code === 200) {
      ElMessage({
        message: '修改用户成功',
        type: 'success',
        plain: true,
      })
      // JSON.parse(localStorage.getItem("User"))更新本地数据
      localStorage.setItem("User", JSON.stringify(res.data))
      // user = JSON.parse(localStorage.getItem("User"))
      centerDialogVisible.value = false
      // 刷新页面
      window.location.reload()
    }
  })
}
  function getUserInfo() {
    no.value = user.no;
    name.value = user.name;
  }




//user可视化数据
const maleCount = ref(0)
const femaleCount = ref(0)
function fetchUserList() {
  proxy.$http.get('/user/list').then(res => res.data).then(res => {
    if (res.code === 200) {
      const users = res.data
      maleCount.value = users.filter(u => u.sex === 1).length
      // console.log( 'maleCount.value',maleCount)
      femaleCount.value = users.filter(u => u.sex === 0).length
      // console.log( 'femaleCount.value',femaleCount)
      initChart()
    }
  })
}
function initChart() {
  const chartDom = document.getElementById('useChart')
  const myChart = echarts.init(chartDom,null, {
    renderer: 'svg'
  })
  const option = {
    color: ['#3398DB', '#FFA500'],
    title: {
      text: '用户性别比例',
      // subtext: '基于数据库 user 表统计',
      left: 'center'
    },
    // tooltip提示框
    tooltip: {
      trigger: 'item',
      formatter: '{a}<br/>{b}: {c}人 (占比：{d}%)' // 显示百分比
    },
    // 图例
    legend: {
      orient: 'vertical',
      left: 'left',
      formatter: (name) => {
        const total = maleCount.value + femaleCount.value
        const maleRate = ((maleCount.value / total) * 100).toFixed(1)
        const femaleRate = ((femaleCount.value / total) * 100).toFixed(1)
        return `${name} ${name === '男性' ? maleRate : femaleRate}%`
      }
    },
    series: [
      {
        name: '性别分布',
        type: 'pie',
        radius: '50%',
        data: [
          { value: maleCount.value, name: '男性' },
          { value: femaleCount.value, name: '女性' }
        ],
        label: {
          show: true,
          position: 'outside',
          formatter: '{b}: {c}人 ({d}%)' // 显示文字标签（名称 + 数量 + 百分比）
        },
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }

  myChart.setOption(option)
}


// 新增响应式变量
const storages = ref([])
const goodstypes = ref([])
const books = ref([])

// 获取所有数据并初始化图表
function fetchDataAndInitChart() {
  Promise.all([
    proxy.$http.get('/storage/list').then(res => res.data),
    proxy.$http.get('/goodstype/list').then(res => res.data),
    proxy.$http.get('/book/list').then(res => res.data)
  ]).then(([storageRes, goodsTypeRes, bookRes]) => {
    if (storageRes.code === 200 && goodsTypeRes.code === 200 && bookRes.code === 200) {
      storages.value = storageRes.data
      goodstypes.value = goodsTypeRes.data
      books.value = bookRes.data

      initBarChart()
    }
  })
}
// 初始化柱状图
function initBarChart() {
  const chartDom = document.getElementById('barChart')
  const myChart = echarts.init(chartDom)

  // 构建 series 数据
  const seriesData = goodstypes.value.map(goodsType => {
    return {
      name: goodsType.name,
      type: 'bar',
      stack: '总量',
      data: storages.value.map(storage => {
        const total = books.value
          .filter(book => book.goodstype === goodsType.id && book.storage === storage.id)
          .reduce((sum, book) => sum + book.count, 0)
        return total
      })
    }
  })

  const option = {
    title: { text: '各仓库中书籍库存数量', left: 'center' },
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    legend: { data: goodstypes.value.map(goodsType => goodsType.name), top: '10%' },
    grid: { left: '3%', right: '4%', bottom: '15%', containLabel: true },
    xAxis: { type: 'category', data: storages.value.map(storage => storage.name) },
    yAxis: { type: 'value' },
    series: seriesData
  }

  myChart.setOption(option)
}



   function initWelcomeChart() {
     const chartDom = document.getElementById('welcomeChart');
     const myChart = echarts.init(chartDom);
     const welcomeText = `欢迎登录，${user.name}`;

     const option = {
       graphic: {
         elements: [
           {
             type: 'text',
             left: 'center',
             top: 'center',
             style: {
               text: welcomeText,
               align: 'center',
               fontSize: 40,
               fontWeight: 'bold',
               lineDash: [0, 200],
               lineDashOffset: 0,
               fill: 'transparent',
               stroke: '#000',
               lineWidth: 1
             },
             keyframeAnimation: {
               duration: 3000,
               loop: true,
               keyframes: [
                 {
                   percent: 0.7,
                   style: {
                     fill: 'transparent',
                     lineDashOffset: 200,
                     lineDash: [200, 0]
                   }
                 },
                 {
                   percent: 0.8,
                   style: {
                     fill: 'transparent'
                   }
                 },
                 {
                   percent: 1,
                   style: {
                     fill: 'black'
                   }
                 }
               ]
             }
           }
         ]
       }
     };

     myChart.setOption(option);
   }


  onMounted(() => {
    getUserInfo();
    fetchUserList();//user可视化数据
    fetchDataAndInitChart();
    initWelcomeChart();
  });


</script>

<style scoped>

</style>
