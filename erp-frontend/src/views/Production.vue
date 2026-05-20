<template>
  <div>
    <el-card>
      <div slot="header" class="clearfix">
        <span style="font-size: 18px; font-weight: bold;">生产管理</span>
        <el-button style="float: right; padding: 3px 0" type="primary" @click="handleAdd">新增生产订单</el-button>
      </div>
      <div style="margin-bottom: 20px;">
        <el-input v-model="searchNo" placeholder="请输入生产单号" style="width: 300px; margin-right: 10px;"></el-input>
        <el-button type="primary" @click="searchOrders">搜索</el-button>
      </div>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="productionNo" label="生产单号" width="140"></el-table-column>
        <el-table-column prop="productName" label="产品名称" width="150"></el-table-column>
        <el-table-column prop="specification" label="规格" width="130"></el-table-column>
        <el-table-column prop="quantity" label="数量" width="80"></el-table-column>
        <el-table-column prop="totalAmount" label="总金额" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.totalAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="120"></el-table-column>
        <el-table-column prop="endDate" label="预计完成" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="responsiblePerson" label="负责人" width="100"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        style="margin-top: 20px; text-align: right;"
        @current-change="handlePageChange"
        :current-page="page"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next">
      </el-pagination>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生产单号" prop="productionNo">
              <el-input v-model="form.productionNo" :disabled="isEdit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产品" prop="productId">
              <el-select v-model="form.productId" placeholder="请选择产品" style="width: 100%" @change="onProductChange" :disabled="isEdit">
                <el-option v-for="p in products" :key="p.id" :label="p.productName" :value="p.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="规格">
              <el-input v-model="form.specification" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单位">
              <el-input v-model="form.unit" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单价">
              <el-input v-model="form.price" disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生产数量" prop="quantity">
              <el-input-number v-model="form.quantity" :min="1" style="width: 100%" @change="calcTotalAmount"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总金额">
              <span style="font-size: 16px; font-weight: bold; color: #f56c6c;">¥{{ form.totalAmount }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker v-model="form.startDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="预计完成" prop="endDate">
              <el-date-picker v-model="form.endDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="实际完成">
              <el-date-picker v-model="form.actualEndDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 100%"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生产状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :label="0">计划中</el-radio>
                <el-radio :label="1">生产中</el-radio>
                <el-radio :label="2">已完成</el-radio>
                <el-radio :label="3">已取消</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="responsiblePerson">
              <el-input v-model="form.responsiblePerson" placeholder="请输入负责人"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Production',
  data() {
    return {
      searchNo: '',
      tableData: [],
      page: 1,
      pageSize: 10,
      total: 0,
      products: [],
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      form: {
        id: null,
        productionNo: '',
        productId: null,
        productName: '',
        specification: '',
        unit: '',
        quantity: 1,
        price: 0,
        totalAmount: 0,
        startDate: '',
        endDate: '',
        actualEndDate: '',
        status: 0,
        responsiblePerson: '',
        remark: ''
      },
      rules: {
        productionNo: [{ required: true, message: '请输入生产单号', trigger: 'blur' }],
        productId: [{ required: true, message: '请选择产品', trigger: 'change' }],
        quantity: [{ required: true, message: '请输入生产数量', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadOrders()
    this.loadProducts()
  },
  methods: {
    loadOrders() {
      this.$axios.get('/api/production-order/list', {
        params: { productionNo: this.searchNo, page: this.page, pageSize: this.pageSize }
      }).then(res => {
        if (res.data.code === 200) {
          this.tableData = res.data.data.list
          this.total = res.data.data.total
        }
      })
    },
    searchOrders() {
      this.page = 1
      this.loadOrders()
    },
    handlePageChange(page) {
      this.page = page
      this.loadOrders()
    },
    loadProducts() {
      this.$axios.get('/api/product/list', { params: { pageSize: 9999 } })
        .then(res => {
          if (res.data.code === 200) {
            this.products = res.data.data.list
          }
        })
    },
    getStatusType(status) {
      const types = ['info', 'warning', 'success', 'danger']
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = ['计划中', '生产中', '已完成', '已取消']
      return texts[status] || '未知'
    },
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增生产订单'
      this.form = {
        id: null,
        productionNo: 'PRD' + Date.now(),
        productId: null,
        productName: '',
        specification: '',
        unit: '',
        quantity: 1,
        price: 0,
        totalAmount: 0,
        startDate: '',
        endDate: '',
        actualEndDate: '',
        status: 0,
        responsiblePerson: '',
        remark: ''
      }
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = '编辑生产订单'
      this.$axios.get(`/api/production-order/get/${row.id}`)
        .then(res => {
          if (res.data.code === 200) {
            this.form = { ...res.data.data }
            this.dialogVisible = true
          }
        })
    },
    onProductChange(productId) {
      const product = this.products.find(p => p.id === productId)
      if (product) {
        this.form.productName = product.productName
        this.form.specification = product.specification
        this.form.unit = product.unit
        this.form.price = product.price
        this.calcTotalAmount()
      }
    },
    calcTotalAmount() {
      this.form.totalAmount = (this.form.price || 0) * (this.form.quantity || 0)
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const url = this.isEdit ? '/api/production-order/update' : '/api/production-order/add'
          this.$axios.post(url, this.form)
            .then(res => {
              if (res.data.code === 200) {
                this.$message.success('操作成功')
                this.dialogVisible = false
                this.loadOrders()
              } else {
                this.$message.error(res.data.message)
              }
            })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确定要删除该生产订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/api/production-order/delete/${row.id}`)
          .then(res => {
            if (res.data.code === 200) {
              this.$message.success('删除成功')
              this.loadOrders()
            } else {
              this.$message.error(res.data.message)
            }
          })
      }).catch(() => {})
    }
  }
}
</script>
