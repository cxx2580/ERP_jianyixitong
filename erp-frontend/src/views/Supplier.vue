<template>
  <div>
    <el-card>
      <div slot="header" class="clearfix">
        <span style="font-size: 18px; font-weight: bold;">供应商管理</span>
        <el-button class="float-right" type="primary" size="small" @click="handleAdd">新增供应商</el-button>
      </div>
      <div style="margin-bottom: 20px;">
        <el-input v-model="searchName" placeholder="请输入供应商名称" style="width: 300px; margin-right: 10px;"></el-input>
        <el-button type="primary" @click="searchSuppliers">搜索</el-button>
      </div>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="supplierNo" label="供应商编号" width="120"></el-table-column>
        <el-table-column prop="supplierName" label="供应商名称" width="180"></el-table-column>
        <el-table-column prop="contact" label="联系人" width="100"></el-table-column>
        <el-table-column prop="phone" label="联系电话" width="130"></el-table-column>
        <el-table-column prop="email" label="邮箱" width="180"></el-table-column>
        <el-table-column prop="address" label="地址"></el-table-column>
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
        <el-form-item label="供应商编号" prop="supplierNo">
          <el-input v-model="form.supplierNo" :disabled="isEdit"></el-input>
        </el-form-item>
        <el-form-item label="供应商名称" prop="supplierName">
          <el-input v-model="form.supplierName"></el-input>
        </el-form-item>
        <el-form-item label="联系人" prop="contact">
          <el-input v-model="form.contact"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address"></el-input>
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
  name: 'Supplier',
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
        supplierNo: '',
        supplierName: '',
        contact: '',
        phone: '',
        email: '',
        address: '',
        status: 1
      },
      rules: {
        supplierNo: [{ required: true, message: '请输入供应商编号', trigger: 'blur' }],
        supplierName: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadSuppliers()
  },
  methods: {
    loadSuppliers() {
      this.$axios.get('/api/supplier/list', {
        params: { supplierName: this.searchName, page: this.page, pageSize: this.pageSize }
      }).then(res => {
        if (res.data.code === 200) {
          this.tableData = res.data.data.list
          this.total = res.data.data.total
        }
      })
    },
    searchSuppliers() {
      this.page = 1
      this.loadSuppliers()
    },
    handlePageChange(page) {
      this.page = page
      this.loadSuppliers()
    },
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增供应商'
      this.form = {
        id: null,
        supplierNo: '',
        supplierName: '',
        contact: '',
        phone: '',
        email: '',
        address: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = '编辑供应商'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const url = this.isEdit ? '/api/supplier/update' : '/api/supplier/add'
          this.$axios.post(url, this.form)
            .then(res => {
              if (res.data.code === 200) {
                this.$message.success('操作成功')
                this.dialogVisible = false
                this.loadSuppliers()
              } else {
                this.$message.error(res.data.message)
              }
            })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确定要删除该供应商吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/api/supplier/delete/${row.id}`)
          .then(res => {
            if (res.data.code === 200) {
              this.$message.success('删除成功')
              this.loadSuppliers()
            } else {
              this.$message.error(res.data.message)
            }
          })
      }).catch(() => {})
    }
  }
}
</script>
