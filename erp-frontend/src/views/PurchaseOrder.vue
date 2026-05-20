<template>
  <div>
    <el-card>
      <div slot="header" class="clearfix">
        <span style="font-size: 18px; font-weight: bold;">采购订单管理</span>
        <el-button class="float-right" type="primary" size="small" @click="handleAdd">新增订单</el-button>
      </div>
      <div style="margin-bottom: 20px;">
        <el-input v-model="searchNo" placeholder="请输入订单编号" style="width: 300px; margin-right: 10px;"></el-input>
        <el-button type="primary" @click="searchOrders">搜索</el-button>
      </div>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="orderNo" label="订单编号" width="150"></el-table-column>
        <el-table-column prop="supplierName" label="供应商名称" width="180"></el-table-column>
        <el-table-column prop="totalAmount" label="总金额" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.totalAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="900px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="订单编号" prop="orderNo">
              <el-input v-model="form.orderNo" :disabled="isEdit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商" prop="supplierId">
              <el-select v-model="form.supplierId" placeholder="请选择供应商" style="width: 100%" @change="onSupplierChange">
                <el-option v-for="s in suppliers" :key="s.id" :label="s.supplierName" :value="s.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="订单状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="0">待审核</el-radio>
            <el-radio :label="1">已审核</el-radio>
            <el-radio :label="2">已入库</el-radio>
            <el-radio :label="3">已完成</el-radio>
            <el-radio :label="4">已取消</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2"></el-input>
        </el-form-item>
        <el-form-item label="订单明细">
          <el-button type="primary" size="small" @click="addItem">添加产品</el-button>
        </el-form-item>
        <el-table :data="items" stripe size="small" style="width: 100%; margin-bottom: 20px;">
          <el-table-column label="产品" width="200">
            <template slot-scope="scope">
              <el-select v-model="scope.row.productId" placeholder="请选择产品" @change="onProductChange(scope.$index)">
                <el-option v-for="p in products" :key="p.id" :label="p.productName" :value="p.id"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="规格" width="120">
            <template slot-scope="scope">
              {{ scope.row.specification }}
            </template>
          </el-table-column>
          <el-table-column label="单位" width="80">
            <template slot-scope="scope">
              {{ scope.row.unit }}
            </template>
          </el-table-column>
          <el-table-column label="单价" width="100">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.price" :min="0" :precision="2" size="small" @change="calculateSubtotal(scope.$index)"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="数量" width="100">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.quantity" :min="1" size="small" @change="calculateSubtotal(scope.$index)"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="小计" width="120">
            <template slot-scope="scope">
              ¥{{ scope.row.subtotal }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template slot-scope="scope">
              <el-button type="danger" size="mini" @click="removeItem(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-form-item label="总金额">
          <span style="font-size: 20px; font-weight: bold; color: #f56c6c;">¥{{ totalAmount }}</span>
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
  name: 'PurchaseOrder',
  data() {
    return {
      searchNo: '',
      tableData: [],
      page: 1,
      pageSize: 10,
      total: 0,
      suppliers: [],
      products: [],
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      form: {
        id: null,
        orderNo: '',
        supplierId: null,
        supplierName: '',
        totalAmount: 0,
        status: 0,
        remark: ''
      },
      items: [],
      rules: {
        orderNo: [{ required: true, message: '请输入订单编号', trigger: 'blur' }],
        supplierId: [{ required: true, message: '请选择供应商', trigger: 'change' }]
      }
    }
  },
  computed: {
    totalAmount() {
      return this.items.reduce((sum, item) => sum + (item.subtotal || 0), 0)
    }
  },
  mounted() {
    this.loadOrders()
    this.loadSuppliers()
    this.loadProducts()
  },
  methods: {
    loadOrders() {
      this.$axios.get('/api/purchase-order/list', {
        params: { orderNo: this.searchNo, page: this.page, pageSize: this.pageSize }
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
    loadSuppliers() {
      this.$axios.get('/api/supplier/list', { params: { pageSize: 9999 } })
        .then(res => {
          if (res.data.code === 200) {
            this.suppliers = res.data.data.list
          }
        })
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
      const types = ['info', 'primary', 'warning', 'success', 'danger']
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = ['待审核', '已审核', '已入库', '已完成', '已取消']
      return texts[status] || '未知'
    },
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增订单'
      this.form = {
        id: null,
        orderNo: 'PO' + Date.now(),
        supplierId: null,
        supplierName: '',
        totalAmount: 0,
        status: 0,
        remark: ''
      }
      this.items = []
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = '编辑订单'
      this.$axios.get(`/api/purchase-order/get/${row.id}`)
        .then(res => {
          if (res.data.code === 200) {
            this.form = res.data.data.order
            this.items = res.data.data.items
            this.dialogVisible = true
          }
        })
    },
    onSupplierChange(supplierId) {
      const supplier = this.suppliers.find(s => s.id === supplierId)
      if (supplier) {
        this.form.supplierName = supplier.supplierName
      }
    },
    onProductChange(index) {
      const product = this.products.find(p => p.id === this.items[index].productId)
      if (product) {
        this.items[index].productName = product.productName
        this.items[index].specification = product.specification
        this.items[index].unit = product.unit
        this.items[index].price = product.price
        this.items[index].quantity = 1
        this.calculateSubtotal(index)
      }
    },
    addItem() {
      this.items.push({
        productId: null,
        productName: '',
        specification: '',
        unit: '',
        price: 0,
        quantity: 1,
        subtotal: 0
      })
    },
    removeItem(index) {
      this.items.splice(index, 1)
    },
    calculateSubtotal(index) {
      const item = this.items[index]
      item.subtotal = (item.price || 0) * (item.quantity || 0)
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.items.length === 0) {
            this.$message.warning('请至少添加一个产品')
            return
          }
          this.form.totalAmount = this.totalAmount
          const url = this.isEdit ? '/api/purchase-order/update' : '/api/purchase-order/add'
          this.$axios.post(url, { order: this.form, items: this.items })
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
      this.$confirm('确定要删除该订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/api/purchase-order/delete/${row.id}`)
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
