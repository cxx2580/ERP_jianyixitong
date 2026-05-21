package com.erp.service;

import com.erp.common.PageResult;
import com.erp.entity.ProductionOrder;
import com.erp.entity.ProductionMaterial;
import com.erp.entity.Material;
import com.erp.entity.Product;
import com.erp.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductionOrderService {
    @Autowired
    private ProductionOrderMapper productionOrderMapper;
    @Autowired
    private ProductionMaterialMapper materialMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private MaterialMapper rawMaterialMapper;
    @Autowired
    private InventoryService inventoryService;

    @Transactional
    public int add(ProductionOrder order, List<ProductionMaterial> materials) {
        int result = productionOrderMapper.insert(order);
        if (materials != null) {
            for (ProductionMaterial m : materials) {
                m.setProductionId(order.getId());
                m.setSubtotal(m.getPrice().multiply(new java.math.BigDecimal(m.getQuantity())));
                materialMapper.insert(m);
            }
        }
        if (order.getStatus() != null && order.getStatus() == 2) {
            completeProduction(order.getId());
        }
        return result;
    }

    @Transactional
    public int update(ProductionOrder order, List<ProductionMaterial> materials) {
        ProductionOrder oldOrder = productionOrderMapper.selectById(order.getId());
        int result = productionOrderMapper.update(order);
        materialMapper.deleteByProductionId(order.getId());
        if (materials != null) {
            for (ProductionMaterial m : materials) {
                m.setProductionId(order.getId());
                m.setSubtotal(m.getPrice().multiply(new java.math.BigDecimal(m.getQuantity())));
                materialMapper.insert(m);
            }
        }
        if (oldOrder.getStatus() != null && order.getStatus() != null
                && !oldOrder.getStatus().equals(order.getStatus())) {
            if (order.getStatus() == 2) {
                completeProduction(order.getId());
            } else if (oldOrder.getStatus() == 2) {
                undoCompleteProduction(order.getId());
            }
        }
        return result;
    }

    @Transactional
    public int delete(Long id) {
        ProductionOrder order = productionOrderMapper.selectById(id);
        if (order != null && order.getStatus() != null && order.getStatus() == 2) {
            undoCompleteProduction(id);
        }
        materialMapper.deleteByProductionId(id);
        return productionOrderMapper.deleteById(id);
    }

    private void completeProduction(Long productionId) {
        ProductionOrder order = productionOrderMapper.selectById(productionId);
        if (order == null) return;
        Product product = productMapper.selectById(order.getProductId());
        if (product != null) {
            product.setStock(product.getStock() + order.getQuantity());
            productMapper.update(product);
            inventoryService.recordChange(order.getProductId(), order.getProductName(),
                    "IN", order.getQuantity(), order.getProductionNo(),
                    order.getResponsiblePerson(), "生产入库" + (order.getSalesOrderNo() != null ? " [关联销售:" + order.getSalesOrderNo() + "]" : ""));
        }
        List<ProductionMaterial> materials = materialMapper.selectByProductionId(productionId);
        if (materials != null) {
            for (ProductionMaterial m : materials) {
                Material rawMat = rawMaterialMapper.selectById(m.getMaterialId());
                if (rawMat != null && m.getQuantity() != null) {
                    rawMat.setStock(rawMat.getStock() - m.getQuantity());
                    rawMaterialMapper.update(rawMat);
                }
            }
        }
    }

    private void undoCompleteProduction(Long productionId) {
        ProductionOrder order = productionOrderMapper.selectById(productionId);
        if (order == null) return;
        Product product = productMapper.selectById(order.getProductId());
        if (product != null) {
            product.setStock(product.getStock() - order.getQuantity());
            productMapper.update(product);
        }
        List<ProductionMaterial> materials = materialMapper.selectByProductionId(productionId);
        if (materials != null) {
            for (ProductionMaterial m : materials) {
                Material rawMat = rawMaterialMapper.selectById(m.getMaterialId());
                if (rawMat != null && m.getQuantity() != null) {
                    rawMat.setStock(rawMat.getStock() + m.getQuantity());
                    rawMaterialMapper.update(rawMat);
                }
            }
        }
    }

    public ProductionOrder getById(Long id) { return productionOrderMapper.selectById(id); }
    public List<ProductionMaterial> getMaterials(Long productionId) { return materialMapper.selectByProductionId(productionId); }

    public PageResult<ProductionOrder> list(String productionNo, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProductionOrder> list = productionOrderMapper.selectList(productionNo, offset, pageSize);
        int total = productionOrderMapper.count(productionNo);
        return PageResult.of(total, list, page, pageSize);
    }
}
