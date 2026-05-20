<template>
  <div class="dashboard">
    <div class="stat-grid">
      <div class="stat-card" v-for="item in statCards" :key="item.label">
        <div class="stat-icon-box" :style="{background: item.iconBg, color: item.iconColor}">
          <i :class="item.icon"></i>
        </div>
        <div class="stat-body">
          <div class="stat-value">{{ item.value }}</div>
          <div class="stat-label">{{ item.label }}</div>
        </div>
      </div>
    </div>

    <div class="mid-row">
      <div class="alert-card" @click="$router.push('/sales-order')">
        <div class="alert-icon-ring" style="background:#EEF1FE; color:#4F6EF7;">
          <i class="el-icon-bell"></i>
        </div>
        <div class="alert-num">{{ summary.pendingOrders || 0 }}</div>
        <div class="alert-text">待处理订单</div>
      </div>
      <div class="alert-card" @click="$router.push('/stock-alert')">
        <div class="alert-icon-ring" style="background:#FEF0F0; color:#E5534B;">
          <i class="el-icon-warning-outline"></i>
        </div>
        <div class="alert-num">{{ summary.lowStockAlerts || 0 }}</div>
        <div class="alert-text">低库存预警</div>
      </div>
      <div class="alert-card">
        <div class="alert-icon-ring" style="background:#E6F7EC; color:#17A86B;">
          <i class="el-icon-box"></i>
        </div>
        <div class="alert-num">{{ summary.productCount || 0 }}</div>
        <div class="alert-text">产品种类</div>
      </div>
    </div>

    <div class="chart-section">
      <div class="section-title">月度销售趋势</div>
      <div ref="chart" class="chart-box"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
export default {
  name: 'Dashboard',
  data() {
    return {
      summary: {},
      statCards: []
    }
  },
  mounted() {
    this.$http.get('/api/dashboard/summary').then(res => {
      this.summary = res.data.data
      this.statCards = [
        { label:'总客户数', value: this.summary.customerCount || 0,
          icon:'el-icon-user', iconBg:'#EEF1FE', iconColor:'#4F6EF7' },
        { label:'总产品数', value: this.summary.productCount || 0,
          icon:'el-icon-goods', iconBg:'#E6F7EC', iconColor:'#17A86B' },
        { label:'本月销售额', value:'¥' + Number(this.summary.monthlySales || 0).toLocaleString(),
          icon:'el-icon-s-finance', iconBg:'#FFF5EB', iconColor:'#D97816' },
        { label:'库存总值', value:'¥' + Number(this.summary.inventoryValue || 0).toLocaleString(),
          icon:'el-icon-s-data', iconBg:'#EEF1FE', iconColor:'#4F6EF7' }
      ]
      this.$nextTick(() => this.renderChart())
    })
  },
  methods: {
    renderChart() {
      const chart = echarts.init(this.$refs.chart)
      const trend = this.summary.salesTrend || []
      chart.setOption({
        tooltip: {
          trigger:'axis',
          backgroundColor:'#fff',
          borderColor:'#e2e5eb',
          textStyle:{ color:'#1a1d26', fontSize:13 },
          boxShadow:'0 2px 12px rgba(0,0,0,0.08)'
        },
        grid: { left:50, right:24, top:24, bottom:28 },
        xAxis: {
          type:'category', data: trend.map(t => t.month),
          axisLine:{ lineStyle:{ color:'#e2e5eb' } },
          axisTick:{ show:false },
          axisLabel:{ color:'#8b919e', fontSize:12, fontWeight:500 }
        },
        yAxis: {
          type:'value', name:'销售额 (元)',
          nameTextStyle:{ color:'#8b919e', fontSize:12 },
          axisLabel:{ color:'#8b919e', fontSize:12, fontWeight:500 },
          splitLine:{ lineStyle:{ color:'#eef0f5', type:'dashed' } },
          axisLine:{ show:false }, axisTick:{ show:false }
        },
        series: [{
          data: trend.map(t => t.amount),
          type:'line', smooth:true, symbol:'circle', symbolSize:6,
          lineStyle:{ width:2.5, color:'#4F6EF7' },
          itemStyle:{ color:'#4F6EF7', borderColor:'#fff', borderWidth:2 },
          areaStyle:{ color:'rgba(79,110,247,0.08)' }
        }]
      })
      window.addEventListener('resize', () => chart.resize())
    }
  }
}
</script>

<style scoped>
.dashboard { max-width:1360px; margin:0 auto; }

.stat-grid {
  display:grid; grid-template-columns:repeat(4, 1fr); gap:16px; margin-bottom:20px;
}
.stat-card {
  background:#fff; border-radius:12px; padding:22px 20px; display:flex; align-items:center;
  gap:16px; border:1px solid #eef0f5;
  box-shadow:0 1px 2px rgba(0,0,0,0.04);
  transition:box-shadow 0.2s ease, border-color 0.2s ease;
}
.stat-card:hover {
  border-color:#d4dae4;
  box-shadow:0 1px 3px rgba(0,0,0,0.06);
}
.stat-icon-box {
  width:48px; height:48px; border-radius:10px; display:flex;
  align-items:center; justify-content:center; font-size:22px; flex-shrink:0;
}
.stat-value {
  font-size:26px; font-weight:650; color:#1a1d26; letter-spacing:-0.3px; line-height:1.2;
}
.stat-label {
  font-size:13px; color:#8b919e; font-weight:500; margin-top:2px;
}

.mid-row {
  display:grid; grid-template-columns:repeat(3, 1fr); gap:16px; margin-bottom:20px;
}
.alert-card {
  background:#fff; border-radius:12px; padding:24px 16px; text-align:center;
  cursor:pointer; border:1px solid #eef0f5;
  box-shadow:0 1px 2px rgba(0,0,0,0.04);
  transition:box-shadow 0.2s ease, border-color 0.2s ease;
}
.alert-card:hover {
  border-color:#d4dae4;
  box-shadow:0 1px 3px rgba(0,0,0,0.06);
}
.alert-icon-ring {
  width:44px; height:44px; border-radius:50%; margin:0 auto 10px;
  display:flex; align-items:center; justify-content:center; font-size:20px;
}
.alert-num { font-size:30px; font-weight:650; color:#1a1d26; line-height:1.2; }
.alert-text { font-size:13px; color:#8b919e; font-weight:500; margin-top:2px; }

.chart-section {
  background:#fff; border-radius:12px; padding:20px 24px 16px;
  border:1px solid #eef0f5;
  box-shadow:0 1px 2px rgba(0,0,0,0.04);
}
.section-title { font-size:16px; font-weight:600; color:#1a1d26; margin-bottom:8px; }
.chart-box { height:340px; }
</style>
