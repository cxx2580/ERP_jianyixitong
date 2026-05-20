<template>
  <div id="app">
    <el-container>
      <el-header style="background-color: #409EFF; color: white;">
        <h1 style="margin: 0; line-height: 60px;">简易ERP系统</h1>
      </el-header>
      <el-container>
        <el-aside width="200px" style="background-color: #f5f7fa;">
          <el-menu
            :default-active="$route.path"
            router
            background-color="#f5f7fa"
            text-color="#333"
            active-text-color="#409EFF">
            <el-menu-item index="/dashboard">
              <i class="el-icon-s-home"></i>
              <span>首页仪表盘</span>
            </el-menu-item>
            <el-submenu index="sales">
              <template slot="title">
                <i class="el-icon-s-shop"></i>
                <span>销售管理</span>
              </template>
              <el-menu-item index="/customer"><span>客户管理</span></el-menu-item>
              <el-menu-item index="/product"><span>产品管理</span></el-menu-item>
              <el-menu-item index="/sales-order"><span>销售订单</span></el-menu-item>
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
              <el-menu-item index="/supplier"><span>供应商管理</span></el-menu-item>
              <el-menu-item index="/purchase-order"><span>采购订单</span></el-menu-item>
            </el-submenu>
            <el-submenu index="inventory">
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
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
#app { font-family: 'Microsoft YaHei', Arial, sans-serif; -webkit-font-smoothing: antialiased; height: 100vh; }
.el-header { padding: 0 20px; }
.el-main { padding: 20px; }
</style>
