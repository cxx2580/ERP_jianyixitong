INSERT INTO customer (customer_no, customer_name, contact, phone, email, address, status) VALUES
('C001', '北京科技有限公司', '张三', '13800138001', 'zhangsan@bjtech.com', '北京市海淀区', 1),
('C002', '上海贸易公司', '李四', '13800138002', 'lisi@shanghaitrade.com', '上海市浦东新区', 1),
('C003', '广州制造公司', '王五', '13800138003', 'wangwu@guangzhoumfg.com', '广州市天河区', 1)
ON DUPLICATE KEY UPDATE customer_name=VALUES(customer_name), contact=VALUES(contact), phone=VALUES(phone), email=VALUES(email), address=VALUES(address), status=VALUES(status);

INSERT INTO product (product_no, product_name, specification, unit, price, stock, status) VALUES
('P001', '笔记本电脑', '15英寸 i5 16G', '台', 5999.00, 100, 1),
('P002', '无线鼠标', '黑色 静音', '个', 99.00, 500, 1),
('P003', '机械键盘', '红轴 104键', '个', 299.00, 200, 1),
('P004', '显示器', '27英寸 4K', '台', 1999.00, 80, 1)
ON DUPLICATE KEY UPDATE product_name=VALUES(product_name), specification=VALUES(specification), unit=VALUES(unit), price=VALUES(price), stock=VALUES(stock), status=VALUES(status);

INSERT INTO production_order (production_no, product_id, product_name, specification, unit, quantity, price, total_amount, start_date, end_date, status, responsible_person, remark) VALUES
('PRD001', 1, '笔记本电脑', '15英寸 i5 16G', '台', 50, 5999.00, 299950.00, '2026-04-01', '2026-04-15', 1, '赵六', '第一批生产计划'),
('PRD002', 3, '机械键盘', '红轴 104键', '个', 100, 299.00, 29900.00, '2026-04-10', '2026-04-20', 0, '钱七', '补充库存'),
('PRD003', 2, '无线鼠标', '黑色 静音', '个', 200, 99.00, 19800.00, '2026-04-05', '2026-04-12', 2, '孙八', '紧急补货')
ON DUPLICATE KEY UPDATE product_id=VALUES(product_id), product_name=VALUES(product_name), specification=VALUES(specification), unit=VALUES(unit), quantity=VALUES(quantity), price=VALUES(price), total_amount=VALUES(total_amount), start_date=VALUES(start_date), end_date=VALUES(end_date), status=VALUES(status), responsible_person=VALUES(responsible_person), remark=VALUES(remark);

INSERT INTO supplier (supplier_no, supplier_name, contact, phone, email, address, status) VALUES
('S001', '深圳电子供应商', '李明', '13900139001', 'liming@szsupplier.com', '深圳市南山区', 1),
('S002', '杭州零件批发商', '刘芳', '13900139002', 'liufang@hzparts.com', '杭州市西湖区', 1),
('S003', '东莞材料公司', '陈伟', '13900139003', 'chenwei@dgmaterial.com', '东莞市虎门镇', 1)
ON DUPLICATE KEY UPDATE supplier_name=VALUES(supplier_name), contact=VALUES(contact), phone=VALUES(phone), email=VALUES(email), address=VALUES(address), status=VALUES(status);

INSERT INTO inventory_record (product_id, product_name, change_type, change_quantity, before_stock, after_stock, related_order_no, operator, remark) VALUES
(1, '笔记本电脑', 'OUT', 20, 100, 80, 'SO20260413001', '张三', '销售出库'),
(3, '机械键盘', 'IN', 100, 200, 300, 'PRD001', '赵六', '生产入库'),
(2, '无线鼠标', 'IN', 50, 500, 550, 'PO20260420001', '李明', '采购入库')
ON DUPLICATE KEY UPDATE product_name=VALUES(product_name), change_type=VALUES(change_type), change_quantity=VALUES(change_quantity);

INSERT INTO stock_alert_config (product_id, product_name, min_stock, alert_enabled) VALUES
(1, '笔记本电脑', 30, 1),
(3, '机械键盘', 50, 1),
(2, '无线鼠标', 100, 1),
(4, '显示器', 20, 1)
ON DUPLICATE KEY UPDATE min_stock=VALUES(min_stock), alert_enabled=VALUES(alert_enabled);
