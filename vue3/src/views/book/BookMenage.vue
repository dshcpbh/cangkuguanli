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
          <el-button type="primary" @click ="addUser" v-if="user.roleId != '2'">新增</el-button>
          <el-button type="primary" @click ="InGoods" v-if="user.roleId != '2'">入库</el-button>
          <el-button type="primary" @click ="OutGoods" v-if="user.roleId != '2'">出库</el-button>
        </div>
      </div>
      <el-table
          highlight-current-row
          @current-change="selectCurrentChange"
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
        <el-table-column prop="name" label="书籍名" width="120" />
        <el-table-column prop="storage" label="仓库名" width="120"
                         :formatter="Formatterstorage"
        />
        <el-table-column prop="goodstype" label="分类名" width="120"
                         :formatter="Formattergoodstype"/>
        <el-table-column prop="count" label="数量" width="60"/>
        <el-table-column prop="createtime" label="创建时间" width="180"
                         :formatter="(row, column, cellValue) => formatTime(cellValue)"
        />
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



      <!--       新增入库弹窗-->
      <el-dialog
          v-model="InGoodsDialogVisible"
          :title="title"
          width="500"
          center
          align-center>
        <el-form prop="form1" ref="form1Ref" :model="form1" label-width="auto" style="max-width: 600px;align-items: center;margin-left: 20px;">
          <el-form-item label="物品名">
            <el-col :span="20">
              <el-input v-model="form1.goodsname"/>
            </el-col>
          </el-form-item>
          <el-form-item label="申请人">
            <el-col :span="20">
              <el-input v-model="form1.username" @click="drawer = true"/>
            </el-col>
          </el-form-item>
          <el-form-item label="数量" prop="count">
            <el-col :span="20">
              <el-input v-model="form1.count"/>
            </el-col>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-col :span="20">
              <el-input v-model="form1.remark" type="textarea" />
            </el-col>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="InGoodsDialogVisible = false">关闭</el-button>
            <el-button type="primary" @click="saveInGoods">
              确认
            </el-button>
          </div>
        </template>
      </el-dialog>
    </el-scrollbar>

<!--    //  抽屉-->
    <el-drawer size="55%" v-model="drawer" title="申请人" :with-header="false">
      <SelectUser @doSelectUser="doSelectUser"></SelectUser>
      <template #footer>
        <div class="dialog-footer" style="margin-right: 10px">
          <el-button @click="drawer = false">关闭</el-button>
          <el-button type="primary" @click="doInUserdrawer">
            确认
          </el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script lang="ts" setup>
import {ref, onMounted, reactive, getCurrentInstance} from 'vue'
import {ComponentSize, ElMessage, FormInstance, FormRules, type UploadUserFile} from 'element-plus'
import SelectUser from '../user/SelectUser.vue'


const tempuser  = ref({})
const drawer = ref(false)
function doSelectUser(val){
  console.log("申请人的选择参数",val)
  tempuser.value = val
}
function doInUserdrawer(){               // 抽屉确认申请人按钮
  form1.userid = tempuser.value.id
  form1.username = tempuser.value.name
  // console.log(form1.userid)
  // console.log(form1.username)
  drawer.value = false
}


const {proxy} = getCurrentInstance()
const title = ref('')
const storage=ref('')
const goodstype=ref('')
let user = JSON.parse(localStorage.getItem("User"));
const innerVisible = ref(false)
function selectUser(){
  innerVisible.value = true
}


// 入库
const currentRow = ref({})
const InGoodsDialogVisible = ref(false)     //form1新增入库弹窗


function selectCurrentChange(row) {
  currentRow.value = row
  console.log("点击书籍管理的行参数",row)
}
const form1Ref = ref<FormInstance>()
const form1 = reactive({
  action: '1',
  goodsname: '',
  username: '',
  userid: '6',
  adminid: '',
  count: '',
  createtime: '',
  remark: '',
  imagepath: '',
})
function resetForm1() {
  if (form1Ref.value) {
    form1Ref.value.resetFields()
  }
  form1.goodsname = ''
  form1.username = ''
  form1.adminid = ''
  form1.count = ''
  form1.createtime = ''
  form1.remark = ''
  form1.imagepath = ''
}
function InGoods() {
  if(!currentRow.value.id){
    ElMessage.error('请选择书籍')
    return
  }
  resetForm1()
  form1.goodsname = currentRow.value.name
  form1.book = currentRow.value.id
  form1.imagepath = currentRow.value.imagepath
  form1.adminid = user.id
  form1.action = '1'
  // console.log(form1.adminid)
  InGoodsDialogVisible.value = true
}
function saveInGoods() {
  proxy.$http.post('/records/save', form1).then(res => res.data).then(res => {
    console.log(res)
    if (res.code === 200) {
      ElMessage({
        message: '入库成功',
        type: 'success',
        plain: true,
      })
      InGoodsDialogVisible.value = false
      getUserList()
      resetForm1()
    }
  })
}
//  出库
function OutGoods() {
  if(!currentRow.value.id){
    ElMessage.error('请选择书籍')
    return
  }
  resetForm1()
  form1.goodsname = currentRow.value.name
  form1.book = currentRow.value.id
  form1.imagepath = currentRow.value.imagepath
  form1.adminid = user.id
  form1.action = '2'
  InGoodsDialogVisible.value = true
}


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
  proxy.$http.get('/book/delete?id='+row.id).then(res => res.data).then(res => {
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
  storage: '',
  goodstype: '',
  count: '',
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
  proxy.$http.post('/book/update', form).then(res => res.data).then(res => {
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
  proxy.$http.post('/book/save', form).then(res => res.data).then(res => {
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


//  获取用户列表
function getUserList() {
  loading.value = true
  proxy.$http.post('/book/queryPage', {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    param: {
      name: name.value+'',
      storage: storage.value+'',
      goodstype: goodstype.value+'',
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
  loadstorage()
  loadgoodstype()
  getUserList()
})
</script>

<style scoped>
:deep(.el-table__body tr.current-row > td) {
  background-color: #ffd87f !important; /* 更醒目的高亮色 */
  font-weight: bold;
}

</style>
