package com.erp.service;

import com.erp.common.PageResult;
import com.erp.entity.Product;
import com.erp.entity.PurchaseOrder;
import com.erp.entity.PurchaseOrderItem;
import com.erp.mapper.ProductMapper;
import com.erp.mapper.PurchaseOrderMapper;
import com.erp.mapper.PurchaseOrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PurchaseOrderService {
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    @Autowired
    private PurchaseOrderItemMapper purchaseOrderItemMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private InventoryService inventoryService;

    @Transactional
    public int add(PurchaseOrder order, List<PurchaseOrderItem> items) {
        int result = purchaseOrderMapper.insert(order);
        if (items != null) {
            for (PurchaseOrderItem item : items) {
                item.setOrderId(order.getId());
                purchaseOrderItemMapper.insert(item);
                increaseStock(item.getProductId(), item.getQuantity());
                inventoryService.recordChange(item.getProductId(), item.getProductName(),
                        "IN", item.getQuantity(), order.getOrderNo(),
                        order.getSupplierName(), "采购入库");
            }
        }
        return result;
    }

    @Transactional
    public int update(PurchaseOrder order, List<PurchaseOrderItem> items) {
        List<PurchaseOrderItem> oldItems = purchaseOrderItemMapper.selectByOrderId(order.getId());
        if (oldItems != null) {
            for (PurchaseOrderItem oldItem : oldItems) {
                decreaseStock(oldItem.getProductId(), oldItem.getQuantity());
            }
        }
        int result = purchaseOrderMapper.update(order);
        purchaseOrderItemMapper.deleteByOrderId(order.getId());
        if (items != null) {
            for (PurchaseOrderItem item : items) {
                item.setOrderId(order.getId());
                purchaseOrderItemMapper.insert(item);
                increaseStock(item.getProductId(), item.getQuantity());
                inventoryService.recordChange(item.getProductId(), item.getProductName(),
                        "IN", item.getQuantity(), order.getOrderNo(),
                        order.getSupplierName(), "采购入库(编辑更新)");
            }
        }
        return result;
    }

    @Transactional
    public int delete(Long id) {
        PurchaseOrder order = purchaseOrderMapper.selectById(id);
        List<PurchaseOrderItem> items = purchaseOrderItemMapper.selectByOrderId(id);
        if (items != null) {
            for (PurchaseOrderItem item : items) {
                decreaseStock(item.getProductId(), item.getQuantity());
                inventoryService.recordChange(item.getProductId(), item.getProductName(),
                        "ADJUST", item.getQuantity(), order != null ? order.getOrderNo() : "",
                        order != null ? order.getSupplierName() : "", "采购订单删除退库");
            }
        }
        purchaseOrderItemMapper.deleteByOrderId(id);
        return purchaseOrderMapper.deleteById(id);
    }

    private void increaseStock(Long productId, Integer quantity) {
        if (productId != null && quantity != null) {
            Product product = productMapper.selectById(productId);
            if (product != null) {
                product.setStock(product.getStock() + quantity);
                productMapper.update(product);
            }
        }
    }

    private void decreaseStock(Long productId, Integer quantity) {
        if (productId != null && quantity != null) {
            Product product = productMapper.selectById(productId);
            if (product != null) {
                product.setStock(product.getStock() - quantity);
                productMapper.update(product);
            }
        }
    }

    public PurchaseOrder getById(Long id) {
        return purchaseOrderMapper.selectById(id);
    }

    public List<PurchaseOrderItem> getItemsByOrderId(Long orderId) {
        return purchaseOrderItemMapper.selectByOrderId(orderId);
    }

    public PageResult<PurchaseOrder> list(String orderNo, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<PurchaseOrder> list = purchaseOrderMapper.selectList(orderNo, offset, pageSize);
        int total = purchaseOrderMapper.count(orderNo);
        return PageResult.of(total, list, page, pageSize);
    }
}
