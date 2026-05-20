<template>
  <div>
    <el-card>
      <div slot="header">
        <span>库存盘点</span>
        <el-input v-model="checkNo" placeholder="盘点单号" size="small" style="width:200px;margin-left:20px" clearable @clear="load" @keyup.enter.native="load"></el-input>
        <el-button type="primary" size="small" @click="load" style="margin-left:10px">查询</el-button>
        <el-button type="success" size="small" @click="showAdd" style="margin-left:10px">新增盘点</el-button>
      </div>
      <el-table :data="tableData" stripe v-loading="loading">
        <el-table-column prop="checkNo" label="盘点单号" width="180"></el-table-column>
        <el-table-column prop="productName" label="产品名称"></el-table-column>
        <el-table-column prop="bookStock" label="账面库存" width="100"></el-table-column>
        <el-table-column prop="actualStock" label="实盘库存" width="100"></el-table-column>
        <el-table-column prop="diffQuantity" label="差异" width="80">
          <template slot-scope="s">
            <span :style="{color: s.row.diffQuantity===0?'#67C23A':'#F56C6C'}">{{ s.row.diffQuantity > 0 ? '+' + s.row.diffQuantity : s.row.diffQuantity }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="s">
            <el-tag :type="s.row.status===1?'success':'info'" size="small">{{ s.row.status === 1 ? '已完成' : '待确认' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operator" label="盘点人" width="100"></el-table-column>
        <el-table-column label="操作" width="120">
          <template slot-scope="s">
            <el-button v-if="s.row.status===0" type="text" size="small" @click="doComplete(s.row)">确认盘点</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:20px" @current-change="onPage" :current-page="page" :page-size="pageSize" layout="total, prev, pager, next" :total="total"></el-pagination>
    </el-card>

    <el-dialog title="新增盘点单" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="选择产品">
          <el-select v-model="form.productId" filterable placeholder="请选择产品" @change="onProductChange" style="width:100%">
            <el-option v-for="p in products" :key="p.id" :label="p.productNo + ' ' + p.productName" :value="p.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="账面库存"><el-input v-model="form.bookStock" disabled></el-input></el-form-item>
        <el-form-item label="实盘数量"><el-input-number v-model="form.actualStock" :min="0" style="width:100%"></el-input-number></el-form-item>
        <el-form-item label="盘点人"><el-input v-model="form.operator"></el-input></el-form-item>
        <el-form-item label="差异">
          <span :style="{color: (form.actualStock - form.bookStock) === 0 ? '#67C23A' : '#F56C6C', fontWeight:'bold'}">
            {{ form.actualStock - form.bookStock > 0 ? '+' + (form.actualStock - form.bookStock) : form.actualStock - form.bookStock }}
          </span>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="doAdd">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'InventoryCheck',
  data() {
    return { tableData: [], products: [], checkNo: '', page: 1, pageSize: 10, total: 0, loading: false,
      dialogVisible: false, form: { productId: null, bookStock: 0, actualStock: 0, operator: '' } }
  },
  mounted() { this.load(); this.loadProducts() },
  methods: {
    load() {
      this.loading = true
      this.$http.get('/api/inventory-check/list', { params: { checkNo: this.checkNo, page: this.page, pageSize: this.pageSize } }).then(res => {
        const d = res.data.data; this.tableData = d.list; this.total = d.total; this.loading = false
      })
    },
    loadProducts() {
      this.$http.get('/api/product/list', { params: { page: 1, pageSize: 100 } }).then(res => {
        this.products = res.data.data.list || []
      })
    },
    onProductChange(pid) {
      const p = this.products.find(pp => pp.id === pid)
      if (p) { this.form.bookStock = p.stock; this.form.productName = p.productName }
    },
    showAdd() { this.form = { productId: null, bookStock: 0, actualStock: 0, operator: '' }; this.dialogVisible = true },
    doAdd() {
      this.$http.post('/api/inventory-check/add', this.form).then(() => {
        this.$message.success('盘点单已创建'); this.dialogVisible = false; this.load()
      })
    },
    doComplete(row) {
      this.$confirm('确认盘点？差异将更新库存。', '提示', { type: 'warning' }).then(() => {
        this.$http.post('/api/inventory-check/complete/' + row.id).then(() => {
          this.$message.success('盘点完成'); this.load()
        })
      }).catch(() => {})
    },
    onPage(p) { this.page = p; this.load() }
  }
}
</script>
