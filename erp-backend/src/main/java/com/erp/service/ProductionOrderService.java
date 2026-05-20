package com.erp.service;

import com.erp.common.PageResult;
import com.erp.entity.ProductionOrder;
import com.erp.entity.Product;
import com.erp.mapper.ProductionOrderMapper;
import com.erp.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductionOrderService {
    @Autowired
    private ProductionOrderMapper productionOrderMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private InventoryService inventoryService;

    @Transactional
    public int add(ProductionOrder order) {
        int result = productionOrderMapper.insert(order);
        if (order.getStatus() != null && order.getStatus() == 2) {
            Product product = productMapper.selectById(order.getProductId());
            if (product != null) {
                product.setStock(product.getStock() + order.getQuantity());
                productMapper.update(product);
            }
        }
        return result;
    }

    @Transactional
    public int update(ProductionOrder order) {
        ProductionOrder oldOrder = productionOrderMapper.selectById(order.getId());
        int result = productionOrderMapper.update(order);
        // 状态变为"已完成"时增加库存，状态从"已完成"变为其他时减少库存
        if (oldOrder.getStatus() != null && order.getStatus() != null
                && !oldOrder.getStatus().equals(order.getStatus())) {
            Product product = productMapper.selectById(order.getProductId());
            if (product != null) {
                if (order.getStatus() == 2) {
                    product.setStock(product.getStock() + order.getQuantity());
                    productMapper.update(product);
                    inventoryService.recordChange(order.getProductId(), order.getProductName(),
                            "IN", order.getQuantity(), order.getProductionNo(),
                            order.getResponsiblePerson(), "生产入库");
                } else if (oldOrder.getStatus() == 2) {
                    product.setStock(product.getStock() - order.getQuantity());
                    productMapper.update(product);
                }
            }
        }
        return result;
    }

    @Transactional
    public int delete(Long id) {
        return productionOrderMapper.deleteById(id);
    }

    public ProductionOrder getById(Long id) {
        return productionOrderMapper.selectById(id);
    }

    public PageResult<ProductionOrder> list(String productionNo, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProductionOrder> list = productionOrderMapper.selectList(productionNo, offset, pageSize);
        int total = productionOrderMapper.count(productionNo);
        return PageResult.of(total, list, page, pageSize);
    }
}
