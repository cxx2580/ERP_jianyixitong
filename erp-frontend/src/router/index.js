import Vue from 'vue'
import VueRouter from 'vue-router'
import Dashboard from '../views/Dashboard.vue'
import Customer from '../views/Customer.vue'
import Product from '../views/Product.vue'
import SalesOrder from '../views/SalesOrder.vue'
import Production from '../views/Production.vue'
import Supplier from '../views/Supplier.vue'
import PurchaseOrder from '../views/PurchaseOrder.vue'
import InventoryOverview from '../views/Inventory.vue'
import InventoryRecord from '../views/InventoryRecord.vue'
import InventoryCheck from '../views/InventoryCheck.vue'
import StockAlert from '../views/StockAlert.vue'
import Material from '../views/Material.vue'

Vue.use(VueRouter)

const routes = [
  { path: '/', redirect: '/dashboard' },
  { path: '/dashboard', name: 'Dashboard', component: Dashboard },
  { path: '/customer', name: 'Customer', component: Customer },
  { path: '/product', name: 'Product', component: Product },
  { path: '/sales-order', name: 'SalesOrder', component: SalesOrder },
  { path: '/production', name: 'Production', component: Production },
  { path: '/supplier', name: 'Supplier', component: Supplier },
  { path: '/purchase-order', name: 'PurchaseOrder', component: PurchaseOrder },
  { path: '/material', name: 'Material', component: Material },
  { path: '/inventory', name: 'Inventory', component: InventoryOverview },
  { path: '/inventory-record', name: 'InventoryRecord', component: InventoryRecord },
  { path: '/inventory-check', name: 'InventoryCheck', component: InventoryCheck },
  { path: '/stock-alert', name: 'StockAlert', component: StockAlert }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
