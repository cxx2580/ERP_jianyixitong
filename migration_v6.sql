-- 物料+生产BOM 数据库迁移脚本
-- 在 Navicat 中选中 erp_db 库，新建查询，粘贴全部执行

CREATE TABLE IF NOT EXISTS material (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    material_no VARCHAR(50) NOT NULL UNIQUE,
    material_name VARCHAR(100) NOT NULL,
    specification VARCHAR(100),
    unit VARCHAR(20),
    price DECIMAL(10,2) NOT NULL,
    stock INT DEFAULT 0,
    status INT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bom (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    material_id BIGINT NOT NULL,
    material_name VARCHAR(100) NOT NULL,
    specification VARCHAR(100),
    unit VARCHAR(20),
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS production_material (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    production_id BIGINT NOT NULL,
    material_id BIGINT NOT NULL,
    material_name VARCHAR(100) NOT NULL,
    specification VARCHAR(100),
    unit VARCHAR(20),
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL
);

ALTER TABLE production_order ADD COLUMN sales_order_id BIGINT;
ALTER TABLE production_order ADD COLUMN sales_order_no VARCHAR(50);

-- 测试数据（如已存在会跳过）
INSERT INTO material (material_no, material_name, specification, unit, price, stock, status) VALUES
('M001', 'CPU芯片', 'i5-12400', '颗', 1200.00, 200, 1),
('M002', '内存条', 'DDR4 16GB', '条', 350.00, 500, 1),
('M003', '固态硬盘', '512GB NVMe', '块', 280.00, 300, 1),
('M004', '机械轴体', 'Cherry红轴', '颗', 2.50, 2000, 1),
('M005', 'PCB板', '104键定制', '块', 45.00, 300, 1),
('M006', '鼠标微动', '欧姆龙', '个', 3.00, 1000, 1);

INSERT INTO bom (product_id, material_id, material_name, specification, unit, price, quantity) VALUES
(1, 1, 'CPU芯片', 'i5-12400', '颗', 1200.00, 1),
(1, 2, '内存条', 'DDR4 16GB', '条', 350.00, 2),
(1, 3, '固态硬盘', '512GB NVMe', '块', 280.00, 1),
(3, 4, '机械轴体', 'Cherry红轴', '颗', 2.50, 104),
(3, 5, 'PCB板', '104键定制', '块', 45.00, 1),
(2, 6, '鼠标微动', '欧姆龙', '个', 3.00, 2);
