<template>
  <div>
    <el-card>
      <div slot="header"><span>低库存预警</span></div>
      <el-table :data="tableData" stripe v-loading="loading">
        <el-table-column prop="productName" label="产品名称"></el-table-column>
        <el-table-column prop="currentStock" label="当前库存" width="100">
          <template slot-scope="s"><span style="color:#F56C6C;font-weight:bold">{{ s.row.currentStock }}</span></template>
        </el-table-column>
        <el-table-column prop="minStock" label="安全阈值" width="100"></el-table-column>
        <el-table-column prop="gap" label="缺口" width="100">
          <template slot-scope="s"><span style="color:#E6A23C">{{ s.row.gap }}</span></template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template slot-scope="s">
            <el-button type="text" size="small" @click="showConfig(s.row)">设置阈值</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!loading && tableData.length===0" description="暂无预警，库存正常"></el-empty>
    </el-card>

    <el-dialog title="设置预警阈值" :visible.sync="dialogVisible" width="400px">
      <el-form :model="configForm" label-width="100px">
        <el-form-item label="产品名称"><el-input :value="configForm.productName" disabled></el-input></el-form-item>
        <el-form-item label="最低库存阈值"><el-input-number v-model="configForm.minStock" :min="0" style="width:100%"></el-input-number></el-form-item>
        <el-form-item label="启用预警"><el-switch v-model="configForm.alertEnabled" :active-value="1" :inactive-value="0"></el-switch></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="doSave">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'StockAlert',
  data() {
    return { tableData: [], loading: false, dialogVisible: false,
      configForm: { productId: null, productName: '', minStock: 0, alertEnabled: 1 } }
  },
  mounted() { this.load() },
  methods: {
    load() {
      this.loading = true
      this.$http.get('/api/stock-alert/list').then(res => {
        this.tableData = res.data.data || []; this.loading = false
      })
    },
    showConfig(row) {
      this.configForm.productId = row.productId
      this.configForm.productName = row.productName
      this.configForm.minStock = row.minStock || 0
      this.configForm.alertEnabled = 1
      this.dialogVisible = true
    },
    doSave() {
      this.$http.post('/api/stock-alert/config', this.configForm).then(() => {
        this.$message.success('保存成功'); this.dialogVisible = false; this.load()
      })
    }
  }
}
</script>
