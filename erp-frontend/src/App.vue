<template>
  <div id="app">
    <el-container>
      <el-header class="app-header">
        <div class="header-left">
          <span class="logo-icon">◇</span>
          <span class="logo-text">ERP System</span>
        </div>
        <div class="header-right">
          <span class="header-tag">进销存管理系统</span>
        </div>
      </el-header>
      <el-container>
        <el-aside class="app-aside" width="220px">
          <el-menu
            :default-active="$route.path"
            router
            class="side-menu"
            text-color="#a8b5c8"
            active-text-color="#fff">
            <el-menu-item index="/dashboard" class="menu-top">
              <i class="el-icon-s-home"></i>
              <span>首页仪表盘</span>
            </el-menu-item>
            <el-submenu index="sales" class="menu-group">
              <template slot="title">
                <i class="el-icon-s-shop"></i>
                <span>销售管理</span>
              </template>
              <el-menu-item index="/customer"><span>客户管理</span></el-menu-item>
              <el-menu-item index="/product"><span>产品管理</span></el-menu-item>
              <el-menu-item index="/sales-order"><span>销售订单</span></el-menu-item>
            </el-submenu>
            <el-menu-item index="/production" class="menu-group">
              <i class="el-icon-s-operation"></i>
              <span>生产管理</span>
            </el-menu-item>
            <el-submenu index="purchase" class="menu-group">
              <template slot="title">
                <i class="el-icon-s-cooperation"></i>
                <span>采购管理</span>
              </template>
              <el-menu-item index="/supplier"><span>供应商管理</span></el-menu-item>
              <el-menu-item index="/purchase-order"><span>采购订单</span></el-menu-item>
            </el-submenu>
            <el-submenu index="inventory" class="menu-group">
              <template slot="title">
                <i class="el-icon-s-data"></i>
                <span>库存管理</span>
              </template>
              <el-menu-item index="/inventory"><span>库存总览</span></el-menu-item>
              <el-menu-item index="/inventory-record"><span>库存流水</span></el-menu-item>
              <el-menu-item index="/inventory-check"><span>库存盘点</span></el-menu-item>
              <el-menu-item index="/stock-alert"><span>低库存预警</span></el-menu-item>
            </el-submenu>
          </el-menu>
        </el-aside>
        <el-main class="app-main">
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default { name: 'App' }
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }

#app {
  font-family: 'PingFang SC', 'Microsoft YaHei', 'Helvetica Neue', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  height: 100vh;
  background: #f0f2f7;
}

.app-header {
  background: linear-gradient(135deg, #1a1f36 0%, #2d3559 50%, #1e2648 100%);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 28px;
  height: 60px !important;
  box-shadow: 0 2px 12px rgba(0,0,0,0.15);
  position: relative;
  z-index: 100;
}
.header-left { display:flex; align-items:center; gap:12px; }
.logo-icon {
  font-size:26px; color:#7b8cff;
  text-shadow: 0 0 20px rgba(123,140,255,0.5);
}
.logo-text {
  font-size:20px; font-weight:700; color:#e8ebf5;
  letter-spacing: 2px;
}
.header-tag {
  color:#8890b5; font-size:13px; letter-spacing:1px;
}

.app-aside {
  background: linear-gradient(180deg, #1c2138 0%, #1a1f35 100%) !important;
  border-right: 1px solid rgba(255,255,255,0.04);
  overflow-x: hidden;
}

.side-menu {
  border-right: none !important;
  background: transparent !important;
}
.side-menu .el-menu-item,
.side-menu .el-submenu__title {
  height: 48px;
  line-height: 48px;
  margin: 3px 10px;
  border-radius: 10px;
  font-size: 14px;
  transition: all 0.25s ease;
}
.side-menu .el-menu-item:hover,
.side-menu .el-submenu__title:hover {
  background: rgba(123,140,255,0.12) !important;
  color: #c8d0e8 !important;
}
.side-menu .el-menu-item.is-active {
  background: linear-gradient(135deg, rgba(123,140,255,0.28) 0%, rgba(118,75,162,0.22) 100%) !important;
  color: #fff !important;
  font-weight: 600;
  box-shadow: inset 3px 0 0 #7b8cff;
}
.side-menu .el-submenu .el-menu-item {
  padding-left: 52px !important;
  height: 40px;
  line-height: 40px;
  font-size: 13px;
  min-width: auto;
}
.side-menu .el-submenu.is-active > .el-submenu__title {
  color: #c8d0e8 !important;
}
.side-menu .el-submenu__icon-arrow {
  color: #5a6388;
}

.app-main {
  padding: 24px;
  background: #f0f2f7;
  min-height: calc(100vh - 60px);
}

/* Global overrides */
.el-card {
  border-radius: 14px !important;
  border: none !important;
  box-shadow: 0 2px 16px rgba(0,0,0,0.06) !important;
  transition: box-shadow 0.3s ease;
}
.el-card:hover {
  box-shadow: 0 4px 24px rgba(0,0,0,0.1) !important;
}
.el-card__header {
  border-bottom: 1px solid #eef0f5 !important;
  font-weight: 600;
  color: #2c3a5e;
  font-size: 15px;
  padding: 16px 20px !important;
}
.el-card__body {
  padding: 20px !important;
}

.el-button--primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  border: none !important;
  box-shadow: 0 3px 10px rgba(102,126,234,0.3);
}
.el-button--primary:hover {
  box-shadow: 0 4px 16px rgba(102,126,234,0.45);
  transform: translateY(-1px);
}
.el-button--success {
  background: linear-gradient(135deg, #5cb78c 0%, #3d9e6e 100%) !important;
  border: none !important;
  box-shadow: 0 3px 10px rgba(92,183,140,0.3);
}
.el-button--danger {
  background: linear-gradient(135deg, #e8836f 0%, #d4526e 100%) !important;
  border: none !important;
}

.el-table th.el-table__cell {
  background: #f7f8fb !important;
  color: #5a6480 !important;
  font-weight: 600 !important;
  font-size: 13px !important;
  border-bottom: 2px solid #e8ebf2 !important;
}
.el-table--striped .el-table__body tr.el-table__row--striped td {
  background: #fafbfd !important;
}

.el-pagination {
  text-align: right;
  margin-top: 16px !important;
}

.el-dialog {
  border-radius: 16px !important;
  box-shadow: 0 20px 50px rgba(0,0,0,0.2) !important;
}

.el-input__inner, .el-select .el-input__inner {
  border-radius: 8px !important;
  border-color: #dfe3ea !important;
}
.el-input__inner:focus, .el-select .el-input__inner:focus {
  border-color: #667eea !important;
  box-shadow: 0 0 0 2px rgba(102,126,234,0.15) !important;
}

.el-table { border: none !important; font-size: 13px !important; }
.el-table::before { display: none !important; }
.el-table th.el-table__cell { border-bottom: 2px solid #e8ebf2 !important; }
.el-table td.el-table__cell { border-bottom: 1px solid #f0f2f5 !important; }

.el-tag { border-radius: 6px !important; border: none !important; font-weight: 500; }

.el-radio__input.is-checked + .el-radio__label { color: #667eea !important; }
.el-radio__input.is-checked .el-radio__inner {
  border-color: #667eea !important; background: #667eea !important;
}

.el-pagination .el-pager li.active {
  background: linear-gradient(135deg, #667eea, #764ba2) !important;
  border-radius: 6px !important; color: #fff !important;
  border: none !important;
}
</style>
