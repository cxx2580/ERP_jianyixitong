package com.erp.controller;

import com.erp.common.PageResult;
import com.erp.common.Result;
import com.erp.entity.InventoryCheck;
import com.erp.service.InventoryCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory-check")
@CrossOrigin
@Validated
public class InventoryCheckController {
    @Autowired
    private InventoryCheckService checkService;

    @PostMapping("/add")
    public Result<Void> add(@RequestBody InventoryCheck check) {
        checkService.add(check);
        return Result.success();
    }

    @PostMapping("/complete/{id}")
    public Result<Void> complete(@PathVariable Long id) {
        checkService.complete(id);
        return Result.success();
    }

    @GetMapping("/get/{id}")
    public Result<InventoryCheck> getById(@PathVariable Long id) {
        return Result.success(checkService.getById(id));
    }

    @GetMapping("/list")
    public Result<PageResult<InventoryCheck>> list(
            @RequestParam(required = false) String checkNo,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(checkService.list(checkNo, page, pageSize));
    }
}
