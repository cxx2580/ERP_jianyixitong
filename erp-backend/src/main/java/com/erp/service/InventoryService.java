package com.erp.service;

import com.erp.common.PageResult;
import com.erp.entity.InventoryRecord;
import com.erp.entity.Product;
import com.erp.mapper.InventoryRecordMapper;
import com.erp.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryRecordMapper recordMapper;
    @Autowired
    private ProductMapper productMapper;

    public void recordChange(Long productId, String productName,
            String changeType, Integer qty, String orderNo,
            String operator, String remark) {
        Product p = productMapper.selectById(productId);
        InventoryRecord rec = new InventoryRecord();
        rec.setProductId(productId);
        rec.setProductName(productName);
        rec.setChangeType(changeType);
        rec.setChangeQuantity(qty);
        rec.setBeforeStock(p != null ? p.getStock() : 0);
        rec.setAfterStock(p != null ? p.getStock() : 0);
        rec.setRelatedOrderNo(orderNo);
        rec.setOperator(operator);
        rec.setRemark(remark);
        recordMapper.insert(rec);
    }

    public PageResult<InventoryRecord> listRecords(String changeType, String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<InventoryRecord> list = recordMapper.selectList(changeType, keyword, offset, pageSize);
        int total = recordMapper.count(changeType, keyword);
        return PageResult.of(total, list, page, pageSize);
    }
}
