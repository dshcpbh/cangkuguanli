<template>
  <div>

    <el-scrollbar>
      <!-- 固定搜索栏 -->
      <div>
        <div style="line-height: 60px;">
          <!--      // 搜索框-->
          <el-input placeholder="请输入书籍名" v-model="name" @keyup.enter="getUserList"
                    style="width: 200px;margin-left: 10px;line-height: 40px;">
          </el-input>
          <!--      性别-->
          <el-select
              v-model="storage"
              filterable
              placeholder="请选择仓库"
              style="width: 200px;margin-left: 10px"
          >
            <el-option
                v-for="item in storageData"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
          <el-select
              v-model="goodstype"
              filterable
              placeholder="请选择分类"
              style="width: 200px;margin-left: 10px"
          >
            <el-option
                v-for="item in goodstypeData"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
          <el-button type="primary" @click="getUserList"
                     style="margin-left: 10px;
        font-size: 16px;">查询
          </el-button>
          <!--      重置-->
          <el-button type="primary" @click="reset">重置</el-button>
<!--          <el-button type="primary" @click ="addUser">新增</el-button>-->
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
        <el-table-column prop="imagepath" label="书籍封面" width="120">
          <template #default="scope">
            <el-image
                :src="scope.row.imagepath ? scope.row.imagepath : 'http://localhost:8080/files/download/OIP.jpg'"
                :preview-src-list="[scope.row.imagepath ? scope.row.imagepath : 'http://localhost:8080/files/download/OIP.jpg']"
                :preview-teleported="true"
            >
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="bookname" label="物品名" width="100" />
        <el-table-column prop="storagename" label="仓库" width="100"/>
        <el-table-column prop="goodstypename" label="分类" width="100"  />
        <el-table-column prop="adminname" label="操作人" width="100" />
        <el-table-column prop="username" label="申请人" width="120" />
        <el-table-column prop="count" label="数量" width="100" />
        <el-table-column prop="createtime" label="申请时间" width="105"
                         :formatter="(row, column, cellValue) => formatTime(cellValue)"
        />
        <el-table-column prop="remark" label="备注" />
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
          <el-form-item label="书籍" prop="name">
            <el-col :span="20">
              <el-input v-model="form.name"/>
            </el-col>
          </el-form-item>
          <el-form-item label="仓库" prop="storage">
            <el-col :span="20">
              <el-select v-model="form.storage" placeholder="请选择仓库" style="width: 308px;margin-left: 5px">
                <el-option
                    v-for="item in storageData"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                />
              </el-select>
            </el-col>
          </el-form-item>
          <el-form-item label="分类" prop="goodstype">
            <el-col :span="20">
              <el-select v-model="form.goodstype" placeholder="请选择分类" style="width: 308px;margin-left: 5px">
                <el-option
                    v-for="item in goodstypeData"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                />
              </el-select>
            </el-col>
          </el-form-item>
          <el-form-item label="数量" prop="count">
            <el-col :span="20">
              <el-input v-model="form.count"/>
            </el-col>
          </el-form-item>
          <el-form-item label="头像" prop="imagepath">
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
const storage=ref('')
const goodstype=ref('')



// 页面数据
const storageData = ref([])
const Formatterstorage = (row) => {
  let temp = storageData.value.find(item => {
    return item.id === row.storage
  })
  return temp ? temp.name : ''
}
const goodstypeData = ref([])
const Formattergoodstype = (row) => {
  let temp = goodstypeData.value.find(item => {
    return item.id === row.goodstype
  })
  return temp ? temp.name : ''
}
function loadstorage(){
  proxy.$http.get('/storage/list').then(res => res.data).then(res => {
    storageData.value = res.data
  })
}
function loadgoodstype(){
  proxy.$http.get('/goodstype/list').then(res => res.data).then(res => {
    goodstypeData.value = res.data
  })
}



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

const formatTime = (timeStr: string): string => {
  if (!timeStr) return '--'

  const date = new Date(timeStr)

  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')

  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}
// 上传头像
const handleAvatarSuccess = (res) => {
  // console.log("res",res)
  form.imagepath = res.data
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
  name: [
    { required: true, message: '请输入名字', trigger: 'blur' },
    { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' },
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
  title.value = '编辑书籍信息'
  clearUploadList()
  centerDialogVisible.value = true
  resetForm()


  form = reactive({
    id: row.id,
    name: row.name,
    storage: row.storage,
    goodstype: row.goodstype,
    count: row.count,
    createtime: row.createtime,
    imagepath: row.imagepath
  })
  // 👇 清除之前的验证提示
  if (ruleFormRef.value) {
    ruleFormRef.value.resetFields()
  }
  // console.log(form.txpath)
  fileList = reactive<UploadUserFile[]>([
    {
      name: '原书籍封面',
      url: row.imagepath,
    },
  ])

}
//  删除用户
function handleDelete(row) {   // 删除用户
  proxy.$http.get('/records/delete?id='+row.id).then(res => res.data).then(res => {
    if (res.code === 200) {
      ElMessage({
        message: '删除书籍成功',
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
  name: '',
  roleid: '',
  storage: '',
  goodstype: '',
  count: '',
  userid: '',
  createtime: '',
  imagepath: 'http://localhost:8080/files/download/OIP.jpg'
})
function resetForm() {// 重置表单
  form.no = ''
  form.name = ''
  form.storage = ''
  form.goodstype = ''
  form.count = ''
  form.createtime = ''
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
  proxy.$http.post('/records/update', form).then(res => res.data).then(res => {
    if (res.code === 200) {
      console.log(res)
      centerDialogVisible.value = false
      ElMessage({
        message: '修改书籍成功',
        type: 'success',
        plain: true,
      })
      getUserList()
      resetForm()
    }
  })
}
function doAdd() {
  proxy.$http.post('/records/save', form).then(res => res.data).then(res => {
    if (res.code === 200) {
      // console.log(res)
      centerDialogVisible.value = false
      ElMessage({
        message: '添加书籍成功',
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
function reset() {            // 重置查询框
  name.value = ''
  storage.value = ''
  goodstype.value = ''
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

let user = JSON.parse(localStorage.getItem("User"));
//  获取用户列表
function getUserList() {
  loading.value = true
  proxy.$http.post('/records/queryPage', {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    param: {
      name: name.value+'',
      storage: storage.value+'',
      goodstype: goodstype.value+'',
      userid:user.id+'',
      roleId:user.roleId+'',
    }
  }).then(res => res.data).then(res => {
    // console.log("出入库管理的全部数据",res.data)
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
  loadstorage()
  loadgoodstype()
  getUserList()
})
</script>




<style scoped>

</style>
