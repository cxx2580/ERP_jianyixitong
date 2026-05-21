# 简易ERP系统

## 项目概述

基于 **Spring Boot + Vue + MyBatis** 的前后端分离 ERP 系统，涵盖企业进销存核心业务流程。包含销售管理、生产管理、采购管理、库存管理、物料管理(BOM)、数据统计仪表盘六大模块。

## 技术栈

### 后端
- Spring Boot 2.7.18
- MyBatis
- MySQL 8.0
- Druid 连接池
- Lombok
- Validation

### 前端
- Vue 2.6.14
- Element UI 2.15.14
- ECharts 6.1.0
- Vue Router
- Axios

## 项目结构

```
ERP_jianyixitong/
├── erp-backend/
│   ├── src/main/java/com/erp/
│   │   ├── ErpApplication.java
│   │   ├── common/          # Result, PageResult, GlobalExceptionHandler
│   │   ├── controller/      # 11 个控制器
│   │   ├── dto/              # ProductionOrderDTO 等
│   │   ├── entity/           # 11 个实体类
│   │   ├── mapper/           # 12 个 Mapper 接口
│   │   └── service/          # 10 个 Service
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   ├── mapper/           # MyBatis XML
│   │   ├── schema.sql        # 建表脚本 (10 表)
│   │   └── data.sql          # 测试数据
│   └── pom.xml
├── erp-frontend/
│   ├── src/
│   │   ├── main.js
│   │   ├── App.vue           # 主布局 (11 个菜单入口)
│   │   ├── router/index.js
│   │   └── views/
│   │       ├── Dashboard.vue       # 首页仪表盘
│   │       ├── Customer.vue        # 客户管理
│   │       ├── Product.vue         # 产品管理
│   │       ├── SalesOrder.vue      # 销售订单 (可创建生产)
│   │       ├── Production.vue      # 生产管理 (物料清单)
│   │       ├── Supplier.vue        # 供应商管理
│   │       ├── PurchaseOrder.vue   # 采购订单
│   │       ├── Material.vue        # 物料管理 + BOM
│   │       ├── Inventory.vue       # 库存总览
│   │       ├── InventoryRecord.vue # 库存流水
│   │       ├── InventoryCheck.vue  # 库存盘点
│   │       └── StockAlert.vue      # 低库存预警
│   └── package.json
└── migration_v6.sql          # 新增表迁移脚本
```

## 快速开始

### 1. 数据库准备

```bash
# 首次使用：执行完整建表+数据脚本
mysql -u root -p < erp-backend/src/main/resources/schema.sql
mysql -u root -p < erp-backend/src/main/resources/data.sql

# 已有旧版本数据库：仅执行迁移脚本
mysql -u root -p < migration_v6.sql
```

### 2. 配置数据库连接

修改 `erp-backend/src/main/resources/application.yml`

### 3. 启动后端

```bash
cd erp-backend
mvn spring-boot:run
# 运行在 http://localhost:8080
```

### 4. 启动前端

```bash
cd erp-frontend
npm install
npm run serve
# 运行在 http://localhost:8081
```

## 功能模块

### 销售管理
- 客户管理 / 产品管理 / 销售订单管理
- 订单状态：待审核 → 已审核 → 已发货 → 已完成 → 已取消
- 销售出库自动扣减库存 + 写库存流水
- **销售订单 → 创建生产**：一键跳转生产页

### 生产管理
- 生产订单管理，关联产品自动回填信息
- 状态：计划中 → 生产中 → 已完成 → 已取消
- **物料清单**：支持从 BOM 加载 + 手动添加物料
- 生产完成自动增加产品库存、扣减物料库存 + 写流水

### 采购管理
- 供应商管理 / 物料管理 / 采购订单管理
- **物料管理**：原材料 CRUD，库存追踪
- **BOM 物料清单**：按产品配置所需物料及用量
- 采购入库自动增加库存 + 写流水

### 库存管理
- 库存总览（低库存红色高亮）/ 库存流水 / 库存盘点 / 低库存预警
- 所有库存变更自动记录流水 (IN/OUT/ADJUST)
- 盘点差异自动调整库存 + 写流水

### 数据统计
- 首页仪表盘：核心指标卡片 + 待办提醒 + 月度销售趋势图
- 销售/生产/采购/库存 四维统计接口

## 数据库表 (10 张)

| 表名 | 说明 |
|------|------|
| `customer` | 客户 |
| `product` | 产品 |
| `sales_order` / `sales_order_item` | 销售订单 + 明细 |
| `production_order` / `production_material` | 生产订单 + 物料消耗 |
| `supplier` | 供应商 |
| `purchase_order` / `purchase_order_item` | 采购订单 + 明细 |
| `material` | 物料/原材料 |
| `bom` | 产品物料清单 |
| `inventory_record` | 库存流水 |
| `inventory_check` | 盘点单 |
| `stock_alert_config` | 库存预警配置 |

## 库存联动规则

| 模块 | 操作 | 效果 |
|------|------|------|
| 销售订单 | 新增/编辑 | 产品库存↓ + OUT 流水 |
| 销售订单 | 删除 | 产品库存↑ + ADJUST 流水 |
| 生产订单 | 完成 | 产品库存↑ + 物料库存↓ + IN 流水 |
| 生产订单 | 取消完成 | 退回产品+物料库存 |
| 采购订单 | 新增/编辑 | 库存↑ + IN 流水 |
| 采购订单 | 删除 | 库存↓ + ADJUST 流水 |
| 库存盘点 | 确认差异 | 库存校正 + ADJUST 流水 |
