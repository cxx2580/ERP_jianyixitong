package com.erp.service;

import com.erp.common.PageResult;
import com.erp.entity.InventoryCheck;
import com.erp.entity.Product;
import com.erp.mapper.InventoryCheckMapper;
import com.erp.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class InventoryCheckService {
    @Autowired
    private InventoryCheckMapper checkMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private InventoryService inventoryService;

    @Transactional
    public int add(InventoryCheck check) {
        check.setCheckNo("CHK" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        Product product = productMapper.selectById(check.getProductId());
        if (product != null) {
            check.setBookStock(product.getStock());
            check.setProductName(product.getProductName());
        }
        check.setDiffQuantity(check.getActualStock() - check.getBookStock());
        check.setStatus(0);
        return checkMapper.insert(check);
    }

    @Transactional
    public int complete(Long id) {
        InventoryCheck check = checkMapper.selectById(id);
        if (check == null || check.getStatus() == 1) return 0;
        check.setStatus(1);
        int result = checkMapper.update(check);
        if (check.getDiffQuantity() != 0) {
            Product product = productMapper.selectById(check.getProductId());
            if (product != null) {
                product.setStock(check.getActualStock());
                productMapper.update(product);
                inventoryService.recordChange(check.getProductId(), check.getProductName(),
                        "ADJUST", Math.abs(check.getDiffQuantity()), check.getCheckNo(),
                        check.getOperator(), "盘点调整：账面" + check.getBookStock() + " → 实盘" + check.getActualStock());
            }
        }
        return result;
    }

    public InventoryCheck getById(Long id) {
        return checkMapper.selectById(id);
    }

    public PageResult<InventoryCheck> list(String checkNo, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<InventoryCheck> list = checkMapper.selectList(checkNo, offset, pageSize);
        int total = checkMapper.count(checkNo);
        return PageResult.of(total, list, page, pageSize);
    }
}
