<template>
  <div>
    <!-- 固定搜索栏 -->
    <div style="position: fixed; top: 50px; left: 200px; right: 0; z-index: 999; background: white; padding: 10px;">
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
        <el-button type="primary" @click ="addUser">新增</el-button>
      </div>
    </div>
    <el-scrollbar>
      <el-table
          :data="tableData"
          border
          style="width: 100%;margin-top: 80px;"
          height="620"
      >
        <el-table-column prop="id" label="id" width="80" />
        <el-table-column prop="no" label="账号" width="120" />
        <el-table-column prop="name" label="姓名" width="120"/>
        <el-table-column prop="sex" label="性别" width="80">
          <template #default="scope">
            <el-tag
                :type="scope.row.sex === 1 ? 'primary' : 'success'"
                disable-transitions
            >{{ scope.row.sex === 1 ? '男' : '女' }}</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="80"/>
        <el-table-column prop="password" label="密码" width="180"/>
        <el-table-column prop="roleId" label="角色" width="120">
          <template #default="scope">
            <el-tag
                :type="scope.row.roleId === 0 ?  'danger' :(scope.row.roleId === 1 ? 'primary' : 'success')"
                disable-transitions>{{ scope.row.roleId === 0 ? '超级管理员' :
                (scope.row.roleId === 1 ? '管理员' : '普通用户') }}</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="130"/>
        <el-table-column prop="operate" label="操作" >
          <template #default="scope">
            <el-button type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-popconfirm
                title="确认删除该用户?"
                @confirm="handleDelete(scope.row)"
            >
              <template #reference>
                <el-button type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
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
      <el-dialog
          v-model="centerDialogVisible"
          title="添加用户"
          width="500"
          align-center
          center
      >
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
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="centerDialogVisible = false">关闭</el-button>
            <el-button type="primary" @click="saveUser">
              确定
            </el-button>
          </div>
        </template>
      </el-dialog>
    </el-scrollbar>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted , reactive} from 'vue'
import axios from 'axios'
import {ComponentSize,  ElMessage} from 'element-plus'

// 表格数据
const tableData = ref([])


function handleEdit(row) {   // 编辑用户
  centerDialogVisible.value = true
  resetForm()
  // console.log(row)
  form = reactive({
    id: row.id,
    no: row.no,
    name: row.name,
    password: row.password,
    age: row.age,
    sex: row.sex+'',
    roleId: row.roleId,
    phone: row.phone
  })
  // console.log(form)
}
function handleDelete(row) {   // 删除用户
  axios.get('/user/delete?id='+row.id).then(res => res.data).then(res => {
    if (res.code === 200) {
      ElMessage({
        message: '删除用户成功',
        type: 'success',
        plain: true,
      })
      getUserList()
    }
  })
}


//  表单数据
let form = reactive({   // 表单数据
  id: '',
  no: '',
  name: '',
  password: '',
  age: '',
  sex: 1,
  roleId: '2',
  phone: ''
})
function resetForm() {// 重置表单
  form.no = ''
  form.name = ''
  form.password = ''
  form.age = ''
  form.sex = '1'
  form.roleId = '2'
  form.phone = ''
}
function saveUser() {        // 确定新增用户保存用户
  if(form.id){
    doUpdate()
  }else {
    doAdd()
  }
}
function doUpdate() {
  axios.post('/user/update', form).then(res => res.data).then(res => {
    if (res.code === 200) {
      console.log(res)
      centerDialogVisible.value = false
      ElMessage({
        message: '修改用户成功',
        type: 'success',
        plain: true,
      })
      getUserList()
      resetForm()
    }
  })
}
function doAdd() {
  axios.post('/user/save', form).then(res => res.data).then(res => {
    if (res.code === 200) {
      // console.log(res)
      centerDialogVisible.value = false
      ElMessage({
        message: '添加用户成功',
        type: 'success',
        plain: true,
      })
      getUserList()
      resetForm()
    }
  })
}


// 查询框v-model="name",性别v-model="sex"
const name = ref('')
const centerDialogVisible = ref(false)// 弹出框
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
function addUser() {      // 新增用户
  resetForm()
  centerDialogVisible.value = true
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
  axios.post( '/user/queryPage', {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    param: {
      name: name.value+'',
      sex: sex.value+''
    }
  }).then(res => res.data).then(res => {
    // console.log(res)
    tableData.value = res.data
    total.value = res.total
  })
}

//  初始化
onMounted(() => {
  getUserList()
})
</script>




<style scoped>

</style>
