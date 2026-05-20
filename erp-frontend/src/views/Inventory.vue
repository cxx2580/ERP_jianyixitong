<template>
  <div>
    <el-card>
      <div slot="header">
        <span>库存总览</span>
        <el-input v-model="keyword" placeholder="搜索产品" style="width:200px;margin-left:20px" clearable @clear="load" @keyup.enter.native="load" size="small"></el-input>
        <el-button type="primary" size="small" @click="load" style="margin-left:10px">搜索</el-button>
      </div>
      <el-table :data="tableData" stripe v-loading="loading">
        <el-table-column prop="productNo" label="产品编号" width="120"></el-table-column>
        <el-table-column prop="productName" label="产品名称"></el-table-column>
        <el-table-column prop="specification" label="规格" width="140"></el-table-column>
        <el-table-column prop="unit" label="单位" width="80"></el-table-column>
        <el-table-column prop="price" label="单价" width="100">
          <template slot-scope="s">¥{{ s.row.price }}</template>
        </el-table-column>
        <el-table-column prop="stock" label="库存量" width="100" sortable>
          <template slot-scope="s">
            <span :style="{color: isLow(s.row) ? '#F56C6C' : '#333', fontWeight: isLow(s.row) ? 'bold' : 'normal'}">
              {{ s.row.stock }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="库存价值" width="120">
          <template slot-scope="s">¥{{ (s.row.price * s.row.stock).toFixed(2) }}</template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:20px" @current-change="onPage" :current-page="page" :page-size="pageSize" layout="total, prev, pager, next" :total="total"></el-pagination>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'Inventory',
  data() {
    return { tableData: [], keyword: '', page: 1, pageSize: 10, total: 0, loading: false, alertConfigs: [] }
  },
  mounted() { this.load(); this.loadAlerts() },
  methods: {
    load() {
      this.loading = true
      this.$http.get('/api/inventory/list', { params: { keyword: this.keyword, page: this.page, pageSize: this.pageSize } }).then(res => {
        const d = res.data.data; this.tableData = d.list; this.total = d.total; this.loading = false
      })
    },
    loadAlerts() {
      this.$http.get('/api/stock-alert/list').then(res => {
        this.alertConfigs = res.data.data || []
      })
    },
    isLow(row) {
      const cfg = this.alertConfigs.find(a => a.productId === row.id)
      return cfg && row.stock < cfg.minStock
    },
    onPage(p) { this.page = p; this.load() }
  }
}
</script>
