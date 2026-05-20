package com.erp.service;

import com.erp.common.PageResult;
import com.erp.entity.Product;
import com.erp.entity.SalesOrder;
import com.erp.entity.SalesOrderItem;
import com.erp.mapper.ProductMapper;
import com.erp.mapper.SalesOrderMapper;
import com.erp.mapper.SalesOrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SalesOrderService {
    @Autowired
    private SalesOrderMapper salesOrderMapper;
    @Autowired
    private SalesOrderItemMapper salesOrderItemMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private InventoryService inventoryService;

    @Transactional
    public int add(SalesOrder order, List<SalesOrderItem> items) {
        int result = salesOrderMapper.insert(order);
        if (items != null) {
            for (SalesOrderItem item : items) {
                item.setOrderId(order.getId());
                salesOrderItemMapper.insert(item);
                deductStock(item.getProductId(), item.getQuantity());
                inventoryService.recordChange(item.getProductId(), item.getProductName(),
                        "OUT", item.getQuantity(), order.getOrderNo(),
                        order.getCustomerName(), "销售出库");
            }
        }
        return result;
    }

    @Transactional
    public int update(SalesOrder order, List<SalesOrderItem> items) {
        List<SalesOrderItem> oldItems = salesOrderItemMapper.selectByOrderId(order.getId());
        if (oldItems != null) {
            for (SalesOrderItem oldItem : oldItems) {
                restoreStock(oldItem.getProductId(), oldItem.getQuantity());
            }
        }
        int result = salesOrderMapper.update(order);
        salesOrderItemMapper.deleteByOrderId(order.getId());
        if (items != null) {
            for (SalesOrderItem item : items) {
                item.setOrderId(order.getId());
                salesOrderItemMapper.insert(item);
                deductStock(item.getProductId(), item.getQuantity());
                inventoryService.recordChange(item.getProductId(), item.getProductName(),
                        "OUT", item.getQuantity(), order.getOrderNo(),
                        order.getCustomerName(), "销售出库(编辑更新)");
            }
        }
        return result;
    }

    @Transactional
    public int delete(Long id) {
        SalesOrder order = salesOrderMapper.selectById(id);
        List<SalesOrderItem> items = salesOrderItemMapper.selectByOrderId(id);
        if (items != null) {
            for (SalesOrderItem item : items) {
                restoreStock(item.getProductId(), item.getQuantity());
                inventoryService.recordChange(item.getProductId(), item.getProductName(),
                        "ADJUST", item.getQuantity(), order != null ? order.getOrderNo() : "",
                        order != null ? order.getCustomerName() : "", "销售订单删除退回");
            }
        }
        salesOrderItemMapper.deleteByOrderId(id);
        return salesOrderMapper.deleteById(id);
    }

    private void deductStock(Long productId, Integer quantity) {
        if (productId != null && quantity != null) {
            Product product = productMapper.selectById(productId);
            if (product != null) {
                product.setStock(product.getStock() - quantity);
                productMapper.update(product);
            }
        }
    }

    private void restoreStock(Long productId, Integer quantity) {
        if (productId != null && quantity != null) {
            Product product = productMapper.selectById(productId);
            if (product != null) {
                product.setStock(product.getStock() + quantity);
                productMapper.update(product);
            }
        }
    }

    public SalesOrder getById(Long id) {
        return salesOrderMapper.selectById(id);
    }

    public List<SalesOrderItem> getItemsByOrderId(Long orderId) {
        return salesOrderItemMapper.selectByOrderId(orderId);
    }

    public PageResult<SalesOrder> list(String orderNo, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<SalesOrder> list = salesOrderMapper.selectList(orderNo, offset, pageSize);
        int total = salesOrderMapper.count(orderNo);
        return PageResult.of(total, list, page, pageSize);
    }
}
