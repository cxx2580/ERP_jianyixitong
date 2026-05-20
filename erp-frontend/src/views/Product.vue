<template>
  <div>
    <el-card>
      <div slot="header" class="clearfix">
        <span style="font-size: 18px; font-weight: bold;">产品管理</span>
        <el-button class="float-right" type="primary" size="small" @click="handleAdd">新增产品</el-button>
      </div>
      <div style="margin-bottom: 20px;">
        <el-input v-model="searchName" placeholder="请输入产品名称" style="width: 300px; margin-right: 10px;"></el-input>
        <el-button type="primary" @click="searchProducts">搜索</el-button>
      </div>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="productNo" label="产品编号" width="120"></el-table-column>
        <el-table-column prop="productName" label="产品名称" width="180"></el-table-column>
        <el-table-column prop="specification" label="规格" width="150"></el-table-column>
        <el-table-column prop="unit" label="单位" width="80"></el-table-column>
        <el-table-column prop="price" label="单价" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="80"></el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="产品编号" prop="productNo">
          <el-input v-model="form.productNo" :disabled="isEdit"></el-input>
        </el-form-item>
        <el-form-item label="产品名称" prop="productName">
          <el-input v-model="form.productName"></el-input>
        </el-form-item>
        <el-form-item label="规格" prop="specification">
          <el-input v-model="form.specification"></el-input>
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit"></el-input>
        </el-form-item>
        <el-form-item label="单价" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input-number v-model="form.stock" :min="0" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
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
  name: 'Product',
  data() {
    return {
      searchName: '',
      tableData: [],
      page: 1,
      pageSize: 10,
      total: 0,
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      form: {
        id: null,
        productNo: '',
        productName: '',
        specification: '',
        unit: '',
        price: 0,
        stock: 0,
        status: 1
      },
      rules: {
        productNo: [{ required: true, message: '请输入产品编号', trigger: 'blur' }],
        productName: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
        price: [{ required: true, message: '请输入单价', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadProducts()
  },
  methods: {
    loadProducts() {
      this.$axios.get('/api/product/list', {
        params: { productName: this.searchName, page: this.page, pageSize: this.pageSize }
      }).then(res => {
        if (res.data.code === 200) {
          this.tableData = res.data.data.list
          this.total = res.data.data.total
        }
      })
    },
    searchProducts() {
      this.page = 1
      this.loadProducts()
    },
    handlePageChange(page) {
      this.page = page
      this.loadProducts()
    },
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增产品'
      this.form = {
        id: null,
        productNo: '',
        productName: '',
        specification: '',
        unit: '',
        price: 0,
        stock: 0,
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = '编辑产品'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const url = this.isEdit ? '/api/product/update' : '/api/product/add'
          this.$axios.post(url, this.form)
            .then(res => {
              if (res.data.code === 200) {
                this.$message.success('操作成功')
                this.dialogVisible = false
                this.loadProducts()
              } else {
                this.$message.error(res.data.message)
              }
            })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确定要删除该产品吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/api/product/delete/${row.id}`)
          .then(res => {
            if (res.data.code === 200) {
              this.$message.success('删除成功')
              this.loadProducts()
            } else {
              this.$message.error(res.data.message)
            }
          })
      }).catch(() => {})
    }
  }
}
</script>
