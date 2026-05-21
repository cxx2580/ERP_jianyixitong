package com.erp.controller;

import com.erp.common.PageResult;
import com.erp.common.Result;
import com.erp.dto.ProductionOrderDTO;
import com.erp.entity.ProductionOrder;
import com.erp.entity.ProductionMaterial;
import com.erp.service.ProductionOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/production-order")
@CrossOrigin
@Validated
public class ProductionOrderController {
    @Autowired
    private ProductionOrderService productionOrderService;

    @PostMapping("/add")
    public Result<Void> add(@RequestBody ProductionOrderDTO dto) {
        productionOrderService.add(dto.getOrder(), dto.getMaterials());
        return Result.success();
    }

    @PostMapping("/update")
    public Result<Void> update(@RequestBody ProductionOrderDTO dto) {
        productionOrderService.update(dto.getOrder(), dto.getMaterials());
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productionOrderService.delete(id);
        return Result.success();
    }

    @GetMapping("/get/{id}")
    public Result<Map<String, Object>> getById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("order", productionOrderService.getById(id));
        result.put("materials", productionOrderService.getMaterials(id));
        return Result.success(result);
    }

    @GetMapping("/list")
    public Result<PageResult<ProductionOrder>> list(@RequestParam(required = false) String productionNo,
                                                     @RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(productionOrderService.list(productionNo, page, pageSize));
    }
}
