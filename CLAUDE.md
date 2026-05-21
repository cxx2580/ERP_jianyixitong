# ERP 简易进销存系统

## Git
- 仓库: git@github.com:cxx2580/ERP_jianyixitong.git
- 分支: master
- 推送前确认: 数据库文件(.mv.db/.lock.db/.trace.db) 和 drawio 文件(.drawio/.drawio.png) 已在 .gitignore 排除
- 不上传: .claude/, node_modules/, target/, data/

## 技术栈
- 后端: Spring Boot 2.7.18 + MyBatis + MySQL 8.0 + Druid + Lombok (JDK 1.8)
- 前端: Vue 2.6.14 + Element UI 2.15.14 + ECharts 6.1 + Axios
- 后端端口 8080, 前端端口 8081

## 项目结构
```
syy dierbufen/
├── erp-backend/     # Spring Boot (11 Controller, 10 Service, 12 Mapper, 11 Entity)
├── erp-frontend/    # Vue 2 (11 页面: Dashboard/Customer/Product/SalesOrder/Production/
                     #   Supplier/PurchaseOrder/Material/Inventory/InventoryRecord/
                     #   InventoryCheck/StockAlert)
├── data/            # 本地数据库 (不入git)
├── drawio图/         # 设计图 (不入git)
└── migration_v6.sql # 新增表迁移脚本
```

## 数据库
- 10 张表: customer, product, sales_order, sales_order_item, production_order,
  production_material, supplier, purchase_order, purchase_order_item, material,
  bom, inventory_record, inventory_check, stock_alert_config
- 已有数据库升级需执行 migration_v6.sql

## 关键业务规则
- 销售出库 → 扣产品库存 + INVENTORY OUT 流水
- 生产完成 → 增产品库存 + 扣物料库存 + INVENTORY IN 流水
- 采购入库 → 增库存 + INVENTORY IN 流水
- 删除订单 → 退回库存 + INVENTORY ADJUST 流水
- 所有库存操作 @Transactional, 通过 InventoryService.recordChange() 统一记录

## 配色
- 主色 #4F6EF7, 日光主题, 字体 PingFang SC/微软雅黑 550-700 weight
