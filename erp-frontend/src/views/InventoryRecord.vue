<template>
  <div>
    <el-card>
      <div slot="header">
        <span>库存流水</span>
        <el-select v-model="changeType" placeholder="变动类型" clearable size="small" style="width:130px;margin-left:20px" @change="load">
          <el-option label="入库 IN" value="IN"></el-option>
          <el-option label="出库 OUT" value="OUT"></el-option>
          <el-option label="调整 ADJUST" value="ADJUST"></el-option>
        </el-select>
        <el-input v-model="keyword" placeholder="产品名称" style="width:180px;margin-left:10px" clearable size="small" @clear="load" @keyup.enter.native="load"></el-input>
        <el-button type="primary" size="small" @click="load" style="margin-left:10px">查询</el-button>
      </div>
      <el-table :data="tableData" stripe v-loading="loading">
        <el-table-column prop="productName" label="产品名称" width="140"></el-table-column>
        <el-table-column prop="changeType" label="类型" width="100">
          <template slot-scope="s">
            <el-tag :type="s.row.changeType==='IN'?'success':s.row.changeType==='OUT'?'danger':'warning'" size="small">
              {{ s.row.changeType === 'IN' ? '入库' : s.row.changeType === 'OUT' ? '出库' : '调整' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="changeQuantity" label="数量" width="80"></el-table-column>
        <el-table-column prop="beforeStock" label="变动前" width="80"></el-table-column>
        <el-table-column prop="afterStock" label="变动后" width="80"></el-table-column>
        <el-table-column prop="relatedOrderNo" label="关联单号" width="180"></el-table-column>
        <el-table-column prop="operator" label="操作人" width="120"></el-table-column>
        <el-table-column prop="remark" label="备注" min-width="160"></el-table-column>
        <el-table-column prop="createTime" label="时间" width="160"></el-table-column>
      </el-table>
      <el-pagination style="margin-top:20px" @current-change="onPage" :current-page="page" :page-size="pageSize" layout="total, prev, pager, next" :total="total"></el-pagination>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'InventoryRecord',
  data() {
    return { tableData: [], changeType: '', keyword: '', page: 1, pageSize: 10, total: 0, loading: false }
  },
  mounted() { this.load() },
  methods: {
    load() {
      this.loading = true
      this.$http.get('/api/inventory/record/list', { params: { changeType: this.changeType, keyword: this.keyword, page: this.page, pageSize: this.pageSize } }).then(res => {
        const d = res.data.data; this.tableData = d.list; this.total = d.total; this.loading = false
      })
    },
    onPage(p) { this.page = p; this.load() }
  }
}
</script>
