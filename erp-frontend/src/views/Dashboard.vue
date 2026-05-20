<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon blue"><i class="el-icon-user"></i></div>
          <div class="stat-info">
            <div class="stat-value">{{ summary.customerCount }}</div>
            <div class="stat-label">总客户数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon green"><i class="el-icon-goods"></i></div>
          <div class="stat-info">
            <div class="stat-value">{{ summary.productCount }}</div>
            <div class="stat-label">总产品数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon orange"><i class="el-icon-s-finance"></i></div>
          <div class="stat-info">
            <div class="stat-value">¥{{ summary.monthlySales | formatNum }}</div>
            <div class="stat-label">本月销售额</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon purple"><i class="el-icon-s-data"></i></div>
          <div class="stat-info">
            <div class="stat-value">¥{{ summary.inventoryValue | formatNum }}</div>
            <div class="stat-label">库存总值</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="8">
        <el-card shadow="hover" class="alert-card" @click.native="$router.push('/sales-order')">
          <div class="alert-num">{{ summary.pendingOrders }}</div>
          <div class="alert-text">待处理订单</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="alert-card warning">
          <div class="alert-num">{{ summary.lowStockAlerts }}</div>
          <div class="alert-text">低库存预警</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="alert-card" @click.native="$router.push('/inventory')">
          <div class="alert-num">{{ summary.productCount }}</div>
          <div class="alert-text">产品种类</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top:20px">
      <div slot="header"><span>月度销售趋势（近12个月）</span></div>
      <div ref="chart" style="height:320px"></div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
export default {
  name: 'Dashboard',
  data() {
    return { summary: {} }
  },
  filters: {
    formatNum(v) { return v ? Number(v).toLocaleString() : '0' }
  },
  mounted() {
    this.$http.get('/api/dashboard/summary').then(res => {
      this.summary = res.data.data
      this.$nextTick(() => this.renderChart())
    })
  },
  methods: {
    renderChart() {
      const chart = echarts.init(this.$refs.chart)
      const trend = this.summary.salesTrend || []
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: trend.map(t => t.month) },
        yAxis: { type: 'value', name: '销售额(元)' },
        series: [{ data: trend.map(t => t.amount), type: 'line', smooth: true,
          areaStyle: { color: 'rgba(64,158,255,0.2)' }, itemStyle: { color: '#409EFF' } }]
      })
    }
  }
}
</script>

<style scoped>
.stat-card { display:flex; align-items:center; cursor:default; }
.stat-icon { width:60px; height:60px; border-radius:50%; display:flex; align-items:center; justify-content:center; font-size:26px; color:#fff; margin-right:15px; }
.stat-icon.blue { background:#409EFF; } .stat-icon.green { background:#67C23A; }
.stat-icon.orange { background:#E6A23C; } .stat-icon.purple { background:#909399; }
.stat-value { font-size:28px; font-weight:bold; color:#333; }
.stat-label { font-size:13px; color:#999; margin-top:4px; }
.alert-card { text-align:center; cursor:pointer; }
.alert-card.warning { background:#fef0f0; }
.alert-num { font-size:36px; font-weight:bold; color:#409EFF; }
.alert-card.warning .alert-num { color:#F56C6C; }
.alert-text { font-size:13px; color:#999; margin-top:4px; }
</style>
