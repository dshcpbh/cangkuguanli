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
          <el-button type="primary" @click ="addUser">新增</el-button>
        </div>
      </div>
      <el-table
          :data="tableData"
          border
          style="width: 100%;margin-top: 5px;"
          max-height="530"
          stripe
          v-loading="loading"
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
        <el-table-column prop="password" label="密码" width="180"/>
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
        <el-table-column prop="phone" label="手机号" width="130"/>
        <el-table-column prop="operate" label="操作" >
          <template #default="scope">
            <el-button type="warning" @click="handleEdit(scope.row)">编辑</el-button>
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
          :title="title"
          width="500"
          align-center
          center
      >
        <el-form ref="ruleFormRef" :rules="rules" :model="form" label-width="auto" style="max-width: 600px;align-items: center;margin-left: 20px;">
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
              <el-input v-model="form.phone" maxlength="11"/>
            </el-col>
          </el-form-item>
          <el-form-item label="头像" prop="txpath">
            <el-upload
                ref="uploadRef"
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
import {ref, onMounted, reactive, getCurrentInstance} from 'vue'
import axios from 'axios'
import {ComponentSize, ElMessage, FormInstance, FormRules, type UploadUserFile} from 'element-plus'

const {proxy} = getCurrentInstance()
const title = ref('')
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
// 上传头像
const handleAvatarSuccess = (res) => {
  // console.log("res",res)
  form.txpath = res.data
  // console.log(form.txpath)
}

const checkNo = (rule, value, callback) => {
  if (form.id) {
    return callback();
  }
  if (!value) {
    return callback(new Error('请输入账号'));
  }

  // 如果未通过前端基本验证，不要发请求
  const len = value.length;
  if (len < 3 || len > 10) {
    return callback(new Error('请先确保账号长度在 3 到 10 个字符之间'));
  }
  // 发起请求检查账号是否已存在
  proxy.$http.get('/user/checkNo?no=' + encodeURIComponent(value))
    .then(res => res.data)
    .then(res => {
      // console.log(res)
      if (res.code === 200) {
        callback(new Error('该账号已存在')); // 账号可用
      } else {
        callback();
      }
    })
    .catch(() => {
      callback(new Error('网络异常，请稍后再试'));
    });
};


// 表单验证规则
const ruleFormRef = ref<FormInstance>()
const rules = reactive<FormRules<typeof form>>({
no: [
  { required: true, message: '请输入账号', trigger: 'blur' },
  { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' },
  { validator: checkNo, trigger: 'blur' }
],
  name: [
    { required: true, message: '请输入名字', trigger: 'blur' },
    { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 16, message: '长度在 6 到 16 个字符', trigger: 'blur' },
  ],
  age: [
    { required: true, message: '请输入年龄', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        const num = Number(value)
        if (isNaN(num)) {
          callback(new Error('请输入有效的数字'))
        } else if (num < 1 || num > 100) {
          callback(new Error('年龄必须在 1 到 100 岁之间'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        const reg = /^1[2-9]\d{9}$/;
        if (!reg.test(value)) {
          callback(new Error('手机号格式错误，需以 1 开头且第二位为 2-9的11位数字'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ]
})

// 编辑用户
let fileList = ref<UploadUserFile[]>([])
const uploadRef = ref() // 获取上传组件实例
// 清空上传文件列表
function clearUploadList() {
  if (uploadRef.value) {
    uploadRef.value.clearFiles()
  }
}
function handleEdit(row) {
  title.value = '编辑用户'
  clearUploadList()
  centerDialogVisible.value = true
  resetForm()


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
  // 👇 清除之前的验证提示
  if (ruleFormRef.value) {
    ruleFormRef.value.resetFields()
  }
  // console.log(form.txpath)
  fileList = reactive<UploadUserFile[]>([
    {
      name: '原头像',
      url: row.txpath,
    },
  ])

}
//  删除用户
function handleDelete(row) {   // 删除用户
  proxy.$http.get('/user/delete?id='+row.id).then(res => res.data).then(res => {
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
  phone: '',
  txpath: 'http://localhost:8080/files/download/OIP.jpg'
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
async function saveUser() {
  try {
    await ruleFormRef.value.validate();
    if (form.id) {
      doUpdate();
    } else {
      doAdd();
    }
  } catch (error) {
    ElMessage.error('表单验证失败');
  }
}
function doUpdate() {
  proxy.$http.post('/user/update', form).then(res => res.data).then(res => {
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
  proxy.$http.post('/user/save', form).then(res => res.data).then(res => {
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
function addUser() {// 新增用户
  title.value = '新增用户'
  resetForm()
  centerDialogVisible.value = true
  // 清除上传组件
  clearUploadList()

  // 👇 清除表单验证提示
  if (ruleFormRef.value) {
    ruleFormRef.value.resetFields()
  }
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
