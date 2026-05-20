<template>
  <div class="dashboard">
    <div class="stat-grid">
      <div class="stat-card" v-for="item in statCards" :key="item.label">
        <div class="stat-icon" :style="{background: item.gradient}">
          <i :class="item.icon"></i>
        </div>
        <div class="stat-body">
          <div class="stat-value">{{ item.value }}</div>
          <div class="stat-label">{{ item.label }}</div>
        </div>
      </div>
    </div>

    <div class="mid-row">
      <div class="alert-card pending" @click="$router.push('/sales-order')">
        <div class="alert-ring">
          <i class="el-icon-bell"></i>
        </div>
        <div class="alert-num">{{ summary.pendingOrders || 0 }}</div>
        <div class="alert-text">待处理订单</div>
      </div>
      <div class="alert-card lowstock" @click="$router.push('/stock-alert')">
        <div class="alert-ring">
          <i class="el-icon-warning-outline"></i>
        </div>
        <div class="alert-num">{{ summary.lowStockAlerts || 0 }}</div>
        <div class="alert-text">低库存预警</div>
      </div>
      <div class="alert-card products">
        <div class="alert-ring">
          <i class="el-icon-box"></i>
        </div>
        <div class="alert-num">{{ summary.productCount || 0 }}</div>
        <div class="alert-text">产品种类</div>
      </div>
    </div>

    <div class="chart-section">
      <div class="chart-header">月度销售趋势</div>
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
        { label:'总客户数', value: this.summary.customerCount || 0, icon:'el-icon-user',
          gradient:'linear-gradient(135deg, #667eea, #764ba2)' },
        { label:'总产品数', value: this.summary.productCount || 0, icon:'el-icon-goods',
          gradient:'linear-gradient(135deg, #5cb78c, #3d9e6e)' },
        { label:'本月销售额', value:'¥' + Number(this.summary.monthlySales || 0).toLocaleString(), icon:'el-icon-s-finance',
          gradient:'linear-gradient(135deg, #f0935b, #e67e22)' },
        { label:'库存总值', value:'¥' + Number(this.summary.inventoryValue || 0).toLocaleString(), icon:'el-icon-s-data',
          gradient:'linear-gradient(135deg, #7b8cff, #5468d4)' }
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
          backgroundColor:'rgba(255,255,255,0.96)',
          borderColor:'#e8ebf2',
          textStyle:{ color:'#333' },
          boxShadow:'0 4px 18px rgba(0,0,0,0.1)'
        },
        grid: { left:40, right:20, top:20, bottom:30 },
        xAxis: {
          type:'category', data: trend.map(t => t.month),
          axisLine:{ lineStyle:{ color:'#dfe3ea' } },
          axisLabel:{ color:'#8890b5' }
        },
        yAxis: {
          type:'value', name:'销售额(元)',
          nameTextStyle:{ color:'#8890b5' },
          axisLabel:{ color:'#8890b5' },
          splitLine:{ lineStyle:{ color:'#f0f2f7', type:'dashed' } }
        },
        series: [{
          data: trend.map(t => t.amount),
          type:'line', smooth:true, symbol:'circle', symbolSize:6,
          lineStyle:{ width:3, color: new echarts.graphic.LinearGradient(0,0,1,0,[
            {offset:0,color:'#667eea'},{offset:1,color:'#764ba2'}
          ])},
          itemStyle:{ color: '#667eea', borderColor:'#fff', borderWidth:2 },
          areaStyle:{ color: new echarts.graphic.LinearGradient(0,0,0,1,[
            {offset:0,color:'rgba(102,126,234,0.25)'},{offset:1,color:'rgba(118,75,162,0.04)'}
          ])}
        }]
      })
      window.addEventListener('resize', () => chart.resize())
    }
  }
}
</script>

<style scoped>
.dashboard { max-width:1400px; margin:0 auto; }

.stat-grid {
  display:grid; grid-template-columns:repeat(4, 1fr); gap:20px; margin-bottom:24px;
}
.stat-card {
  background:#fff; border-radius:16px; padding:24px; display:flex; align-items:center;
  gap:18px; box-shadow:0 2px 16px rgba(0,0,0,0.05);
  transition:all 0.3s ease; cursor:default;
}
.stat-card:hover { transform:translateY(-3px); box-shadow:0 8px 28px rgba(0,0,0,0.1); }
.stat-icon {
  width:56px; height:56px; border-radius:14px; display:flex; align-items:center;
  justify-content:center; font-size:26px; color:#fff; flex-shrink:0;
}
.stat-value { font-size:28px; font-weight:700; color:#1e2648; letter-spacing:-0.5px; }
.stat-label { font-size:13px; color:#8890b5; margin-top:4px; }

.mid-row { display:grid; grid-template-columns:repeat(3, 1fr); gap:20px; margin-bottom:24px; }
.alert-card {
  background:#fff; border-radius:16px; padding:28px 20px; text-align:center;
  cursor:pointer; transition:all 0.3s ease;
  box-shadow:0 2px 16px rgba(0,0,0,0.05);
}
.alert-card:hover { transform:translateY(-3px); box-shadow:0 8px 28px rgba(0,0,0,0.1); }
.alert-ring {
  width:48px; height:48px; border-radius:50%; margin:0 auto 12px;
  display:flex; align-items:center; justify-content:center; font-size:22px;
}
.pending .alert-ring { background:rgba(102,126,234,0.12); color:#667eea; }
.lowstock .alert-ring { background:rgba(232,131,111,0.12); color:#e8836f; }
.products .alert-ring { background:rgba(92,183,140,0.12); color:#5cb78c; }
.alert-num { font-size:32px; font-weight:700; color:#1e2648; }
.alert-text { font-size:13px; color:#8890b5; margin-top:4px; }

.chart-section {
  background:#fff; border-radius:16px; padding:24px;
  box-shadow:0 2px 16px rgba(0,0,0,0.05);
}
.chart-header { font-size:16px; font-weight:600; color:#2c3a5e; margin-bottom:16px; }
.chart-box { height:340px; }
</style>
