<template>
  <div id="app">
    <el-container>
      <el-header class="app-header">
        <div class="header-left">
          <svg class="logo-mark" width="28" height="28" viewBox="0 0 28 28" fill="none">
            <rect width="28" height="28" rx="6" fill="#4F6EF7"/>
            <path d="M7 14L12 19L21 9" stroke="white" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <span class="logo-text">ERP System</span>
        </div>
        <div class="header-right">
          <span class="header-subtitle">进销存管理系统</span>
        </div>
      </el-header>
      <el-container>
        <el-aside class="app-aside" width="220px">
          <el-menu
            :default-active="$route.path"
            router
            class="side-menu">
            <el-menu-item index="/dashboard" class="menu-item-top">
              <i class="el-icon-s-home"></i>
              <span>首页仪表盘</span>
            </el-menu-item>

            <el-submenu index="sales">
              <template slot="title">
                <i class="el-icon-s-shop"></i>
                <span>销售管理</span>
              </template>
              <el-menu-item index="/customer">客户管理</el-menu-item>
              <el-menu-item index="/product">产品管理</el-menu-item>
              <el-menu-item index="/sales-order">销售订单</el-menu-item>
            </el-submenu>

            <el-menu-item index="/production">
              <i class="el-icon-s-operation"></i>
              <span>生产管理</span>
            </el-menu-item>

            <el-submenu index="purchase">
              <template slot="title">
                <i class="el-icon-s-cooperation"></i>
                <span>采购管理</span>
              </template>
              <el-menu-item index="/supplier">供应商管理</el-menu-item>
              <el-menu-item index="/purchase-order">采购订单</el-menu-item>
            </el-submenu>

            <el-submenu index="inventory">
              <template slot="title">
                <i class="el-icon-s-data"></i>
                <span>库存管理</span>
              </template>
              <el-menu-item index="/inventory">库存总览</el-menu-item>
              <el-menu-item index="/inventory-record">库存流水</el-menu-item>
              <el-menu-item index="/inventory-check">库存盘点</el-menu-item>
              <el-menu-item index="/stock-alert">低库存预警</el-menu-item>
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

:root {
  --color-accent: #4F6EF7;
  --color-accent-hover: #3D5CD9;
  --color-accent-light: #EEF1FE;
  --color-text: #1a1d26;
  --color-text-secondary: #5f6677;
  --color-text-muted: #8b919e;
  --color-border: #e2e5eb;
  --color-border-light: #eef0f5;
  --color-bg: #f7f8fb;
  --color-bg-white: #ffffff;
  --color-sidebar-bg: #fafbfc;
  --radius-sm: 8px;
  --radius-md: 10px;
  --radius-lg: 12px;
  --shadow-sm: 0 1px 2px rgba(0,0,0,0.04);
  --shadow-md: 0 1px 3px rgba(0,0,0,0.06), 0 1px 2px rgba(0,0,0,0.04);
}

#app {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Microsoft YaHei', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100vh;
  background: var(--color-bg);
  font-weight: 450;
  color: var(--color-text);
  font-size: 14px;
  line-height: 1.5;
}

/* ---- Header ---- */
.app-header {
  background: var(--color-bg-white);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 28px;
  height: 56px !important;
  border-bottom: 1px solid var(--color-border);
  box-shadow: none;
}
.header-left { display:flex; align-items:center; gap:10px; }
.logo-mark { flex-shrink:0; }
.logo-text {
  font-size:17px; font-weight:600; color:var(--color-text);
  letter-spacing: -0.2px;
}
.header-subtitle {
  color:var(--color-text-muted); font-size:13px; font-weight:450;
}

/* ---- Sidebar ---- */
.app-aside {
  background: var(--color-sidebar-bg) !important;
  border-right: 1px solid var(--color-border-light);
  overflow-x: hidden;
}
.side-menu {
  border-right: none !important;
  background: transparent !important;
}
.side-menu .el-menu-item,
.side-menu .el-submenu__title {
  height: 44px;
  line-height: 44px;
  margin: 2px 8px;
  border-radius: var(--radius-md);
  font-size: 14px;
  font-weight: 450;
  color: var(--color-text-secondary);
  padding-left: 16px !important;
  transition: all 0.15s ease;
  border-left: 3px solid transparent;
}
.side-menu .el-menu-item:hover,
.side-menu .el-submenu__title:hover {
  background: var(--color-accent-light) !important;
  color: var(--color-accent) !important;
}
.side-menu .el-menu-item.is-active {
  background: var(--color-accent-light) !important;
  color: var(--color-accent) !important;
  font-weight: 600;
  border-left: 3px solid var(--color-accent);
}
.side-menu .el-submenu .el-menu-item {
  padding-left: 48px !important;
  height: 38px;
  line-height: 38px;
  font-size: 13px;
  border-left: 3px solid transparent;
}
.side-menu .el-submenu .el-menu-item.is-active {
  border-left: 3px solid var(--color-accent);
}
.side-menu .el-submenu.is-active > .el-submenu__title {
  color: var(--color-text) !important;
  font-weight: 500;
}
.side-menu .el-submenu__icon-arrow {
  color: var(--color-text-muted);
}
.side-menu .el-menu-item i,
.side-menu .el-submenu__title i {
  color: var(--color-text-muted);
  margin-right: 8px;
}
.side-menu .el-menu-item.is-active i,
.side-menu .el-menu-item:hover i {
  color: var(--color-accent);
}

/* ---- Main ---- */
.app-main {
  padding: 24px 28px;
  background: var(--color-bg);
  min-height: calc(100vh - 56px);
}

/* ---- Global: Card ---- */
.el-card {
  border-radius: var(--radius-lg) !important;
  border: 1px solid var(--color-border-light) !important;
  box-shadow: var(--shadow-sm) !important;
  background: var(--color-bg-white) !important;
}
.el-card__header {
  border-bottom: 1px solid var(--color-border-light) !important;
  font-weight: 600;
  color: var(--color-text);
  font-size: 15px;
  padding: 14px 20px !important;
}
.el-card__body { padding: 20px !important; }

/* ---- Global: Button ---- */
.el-button {
  font-weight: 500;
  border-radius: var(--radius-sm);
  transition: all 0.15s ease;
}
.el-button--primary {
  background: var(--color-accent) !important;
  border: 1px solid var(--color-accent) !important;
  color: #fff !important;
  box-shadow: none;
}
.el-button--primary:hover {
  background: var(--color-accent-hover) !important;
  border-color: var(--color-accent-hover) !important;
}
.el-button--success {
  background: #17A86B !important;
  border-color: #17A86B !important;
  color: #fff !important;
}
.el-button--danger {
  background: #E5534B !important;
  border-color: #E5534B !important;
  color: #fff !important;
}

/* ---- Global: Table ---- */
.el-table {
  border: none !important;
  font-size: 14px !important;
  color: var(--color-text);
}
.el-table::before { display: none !important; }
.el-table th.el-table__cell {
  background: #f9fafc !important;
  color: var(--color-text-secondary) !important;
  font-weight: 600 !important;
  font-size: 13px !important;
  border-bottom: 1px solid var(--color-border-light) !important;
  padding: 10px 0 !important;
}
.el-table td.el-table__cell {
  border-bottom: 1px solid var(--color-border-light) !important;
  padding: 12px 0 !important;
}
.el-table--striped .el-table__body tr.el-table__row--striped td {
  background: #fafbfd !important;
}
.el-table__body tr:hover > td {
  background: var(--color-accent-light) !important;
}

/* ---- Global: Tag ---- */
.el-tag {
  border-radius: 6px !important;
  border: none !important;
  font-weight: 500;
  font-size: 12px;
}
.el-tag--success { background: #e6f7ec !important; color: #17A86B !important; }
.el-tag--danger { background: #fef0f0 !important; color: #E5534B !important; }
.el-tag--warning { background: #fef7e6 !important; color: #C27C16 !important; }
.el-tag--info { background: #eff1f5 !important; color: var(--color-text-secondary) !important; }

/* ---- Global: Input ---- */
.el-input__inner {
  border-radius: var(--radius-sm) !important;
  border: 1px solid var(--color-border) !important;
  font-size: 14px !important;
  color: var(--color-text);
}
.el-input__inner:focus {
  border-color: var(--color-accent) !important;
  box-shadow: 0 0 0 3px rgba(79,110,247,0.1) !important;
}
.el-input__inner:hover {
  border-color: #c4cad4;
}

/* ---- Global: Select ---- */
.el-select .el-input.is-focus .el-input__inner {
  border-color: var(--color-accent) !important;
}

/* ---- Global: Radio ---- */
.el-radio__input.is-checked + .el-radio__label { color: var(--color-text) !important; font-weight: 500; }
.el-radio__input.is-checked .el-radio__inner {
  border-color: var(--color-accent) !important;
  background: var(--color-accent) !important;
}

/* ---- Global: Pagination ---- */
.el-pagination {
  text-align: right;
  margin-top: 16px !important;
  font-weight: 450;
}
.el-pagination .el-pager li.active {
  background: var(--color-accent) !important;
  border-radius: 6px !important;
  color: #fff !important;
  border: none !important;
  font-weight: 600;
}
.el-pagination button, .el-pagination .el-pager li {
  font-weight: 500;
  border-radius: var(--radius-sm);
}

/* ---- Global: Dialog ---- */
.el-dialog {
  border-radius: var(--radius-lg) !important;
  box-shadow: 0 4px 24px rgba(0,0,0,0.12) !important;
}
.el-dialog__header {
  font-weight: 600;
  font-size: 16px;
  padding: 20px 24px 16px !important;
}
.el-dialog__body { padding: 16px 24px !important; }

/* ---- Global: Form ---- */
.el-form-item__label {
  color: var(--color-text) !important;
  font-weight: 500 !important;
}

/* ---- Scrollbar ---- */
::-webkit-scrollbar { width:6px; }
::-webkit-scrollbar-track { background:transparent; }
::-webkit-scrollbar-thumb { background:#d4d8e0; border-radius:3px; }
::-webkit-scrollbar-thumb:hover { background:#bcc2cd; }
</style>
