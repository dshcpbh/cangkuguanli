<template>
  <div>

    <el-scrollbar>
      <!-- 固定搜索栏 -->
      <div>
        <div style="line-height: 60px;">
          <!--      // 搜索框-->
          <el-input placeholder="请输入分类名" v-model="name" @keyup.enter="getUserList"
                    style="width: 200px;margin-left: 10px;line-height: 40px;">
          </el-input>
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
        <el-table-column prop="id" label="id" width="80"/>
        <el-table-column prop="name" label="分类名" width="100"/>
        <el-table-column prop="remark" label="备注" />
        <el-table-column prop="operate" label="操作">
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
          :title="title"
          width="500"
          align-center
          center
      >
        <el-form ref="ruleFormRef" :rules="rules" :model="form" label-width="auto" style="max-width: 600px;align-items: center;margin-left: 20px;">
          <el-form-item label="分类名" prop="name">
            <el-col :span="20">
              <el-input v-model="form.name"/>
            </el-col>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-col :span="20">
              <el-input type="textarea" v-model="form.remark"/>
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
import {ref, onMounted, reactive, getCurrentInstance} from 'vue'
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

// 表单验证规则
const ruleFormRef = ref<FormInstance>()
const rules = reactive<FormRules<typeof form>>({
  name: [
    { required: true, message: '请输入名字', trigger: 'blur' },
    { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' },
  ],
})

function handleEdit(row) {
  console.log(row)
  title.value = '编辑分类'
  centerDialogVisible.value = true
  resetForm()

  form = reactive({
    id: row.id,
    name: row.name,
    remark: row.remark,
  })
  // 👇 清除之前的验证提示
  if (ruleFormRef.value) {
    ruleFormRef.value.resetFields()
  }
}
//  删除用户
function handleDelete(row) {   // 删除用户
  proxy.$http.get('/goodstype/delete?id='+row.id).then(res => res.data).then(res => {
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
  name: '',
  remark: '',
})
function resetForm() {// 重置表单
  form.id = ''
  form.name = ''
  form.remark = ''
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
  proxy.$http.post('/goodstype/update', form).then(res => res.data).then(res => {
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
  proxy.$http.post('/goodstype/save', form).then(res => res.data).then(res => {
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
const sex = ref('')
function reset() {            // 重置查询框
  name.value = ''
  sex.value = ''
  getUserList()
}
function addUser() {// 新增用户
  title.value = '新增分类'
  resetForm()
  centerDialogVisible.value = true
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
  proxy.$http.post('/goodstype/queryPage', {
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
