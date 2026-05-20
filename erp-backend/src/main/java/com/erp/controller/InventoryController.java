package com.erp.controller;

import com.erp.common.PageResult;
import com.erp.common.Result;
import com.erp.entity.InventoryRecord;
import com.erp.entity.Product;
import com.erp.service.InventoryService;
import com.erp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin
@Validated
public class InventoryController {
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/list")
    public Result<PageResult<Product>> list(@RequestParam(required = false) String keyword,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(productService.list(keyword, page, pageSize));
    }

    @GetMapping("/get/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        return Result.success(productService.getById(id));
    }

    @GetMapping("/record/list")
    public Result<PageResult<InventoryRecord>> listRecords(
            @RequestParam(required = false) String changeType,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(inventoryService.listRecords(changeType, keyword, page, pageSize));
    }
}
