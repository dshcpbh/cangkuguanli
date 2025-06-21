<template>
  <div>

    <el-scrollbar>
      <!-- 固定搜索栏 -->
      <div>
        <div style="line-height: 60px;">
          <!--      // 搜索框-->
          <el-input placeholder="请输入名字" v-model="name" @keyup.enter="getUserList"
                    style="width: 200px;margin-left: 10px;line-height: 40px;">
          </el-input>
          <!--      性别-->
          <el-select
              v-model="sex"
              filterable
              placeholder="请选择性别"
              style="width: 200px;margin-left: 10px"
          >
            <el-option
                v-for="item in sexs"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
          <el-button type="primary" @click="getUserList"
                     style="margin-left: 10px;
        font-size: 16px;">查询
          </el-button>
          <!--      重置-->
          <el-button type="primary" @click="reset">重置</el-button>
        </div>
      </div>
      <el-table
          :data="tableData"
          border
          style="width: 100%;margin-top: 5px;"
          max-height="515"
          stripe
          v-loading="loading"
          highlight-current-row
          @current-change="selectCurrentChange"
          :element-loading-svg="svg"
          class="custom-loading-svg"
          element-loading-svg-view-box="-10, -10, 50, 50"
          :header-row-style="{ height: '50px', background: '#13376c', color: '#cc0505' }"
          :header-cell-style="{ background: '#63bfad', color: '#fff', fontWeight: 'bold', fontSize: '16px' }"
      >
        <el-table-column prop="id" label="id" width="80" />
        <el-table-column label="头像" width="80">
          <template #default="scope">
            <el-image
                style="width: 50px; height: 50px; border-radius: 50%;"
                :src="scope.row.txpath ? scope.row.txpath : 'http://localhost:8080/files/download/OIP.jpg'"
                :preview-src-list="[scope.row.txpath ? scope.row.txpath : 'http://localhost:8080/files/download/OIP.jpg']"
                :preview-teleported="true"
            >
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="no" label="账号" width="120" />
        <el-table-column prop="name" label="姓名" width="120"/>
        <el-table-column prop="sex" label="性别" width="60">
          <template #default="scope">
            <el-tag
                effect="dark"
                :type="scope.row.sex === 1 ? 'primary' : 'success'"
                disable-transitions
            >{{ scope.row.sex === 1 ? '男' : '女' }}</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="60"/>
        <el-table-column prop="roleId" label="角色" width="100">
          <template #default="scope">
            <el-tag
                effect="dark"
                :type="scope.row.roleId === 0 ?  'danger' :(scope.row.roleId === 1 ? 'primary' : 'success')"
                disable-transitions>{{ scope.row.roleId === 0 ? '超级管理员' :
                (scope.row.roleId === 1 ? '管理员' : '普通用户') }}</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号"/>
      </el-table>
      <!--        分页-->
      <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20 ,30, 40]"
          :size="size"
          :disabled="disabled"
          :background="background"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          style="margin-left: 25%"
      />
    </el-scrollbar>
  </div>
</template>

<script lang="ts" setup>
import {ref, onMounted, reactive, getCurrentInstance} from 'vue'
import {ComponentSize, ElMessage, FormInstance, FormRules, type UploadUserFile} from 'element-plus'

const {proxy} = getCurrentInstance()
// 表格数据
const tableData = ref([])
const loading = ref(false)
const svg = `
        <path class="path" d="
          M 30 15
          L 28 17
          M 25.61 25.61
          A 15 15, 0, 0, 1, 15 30
          A 15 15, 0, 1, 1, 27.99 7.5
          L 15 15
        " style="stroke-width: 4px; fill: rgba(0, 0, 0, 0)"/>
      `
const emit = defineEmits(['doSelectUser'])
function selectCurrentChange(val){
      emit('doSelectUser', val)
}

// 查询框v-model="name",性别v-model="sex"
const name = ref('')
const sexs = ref([
  {
    value: 1,
    label: '男'
  },
  {
    value: 0,
    label: '女'
  }
])
const sex = ref('')
function reset() {            // 重置查询框
  name.value = ''
  sex.value = ''
  getUserList()
}


// 分页
const total = ref(0)
const pageSize = ref(10)//  每页条数
const pageNum = ref(1)// 当前页数
const size = ref<ComponentSize>('default')// 尺寸
const background = ref(false)// 背景色
const disabled = ref(false)// 禁用
const handleSizeChange = (val) => {
  // console.log(`每页 ${val} 条`);
  pageNum.value = 1
  pageSize.value = val
  getUserList()
}
const handleCurrentChange = (val) => {
  // console.log(`当前页: ${val}`);
  pageNum.value = val
  getUserList()
}


//  获取用户列表
function getUserList() {
  loading.value = true
  proxy.$http.post('/user/queryPage', {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    param: {
      name: name.value+'',
      sex: sex.value+'',
      roleId: '2'
    }
  }).then(res => res.data).then(res => {
    // console.log(res)
    tableData.value = res.data
    total.value = res.total
  }).catch(error => {
    ElMessage.error('数据加载失败')
    console.error(error)
  }).finally(() => {
    // 使用 setTimeout 延迟关闭 loading
    setTimeout(() => {
      loading.value = false
    }, 500) // 0.5秒后关闭 loading
  })
}

//  初始化
onMounted(() => {
  getUserList()
})
</script>



<style scoped>

</style>
