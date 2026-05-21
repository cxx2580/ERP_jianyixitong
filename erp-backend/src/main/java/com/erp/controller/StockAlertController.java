package com.erp.controller;

import com.erp.common.Result;
import com.erp.entity.StockAlertConfig;
import com.erp.service.StockAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stock-alert")
@CrossOrigin
@Validated
public class StockAlertController {
    @Autowired
    private StockAlertService alertService;

    @PostMapping("/config")
    public Result<Void> saveConfig(@RequestBody StockAlertConfig config) {
        if (config.getProductId() == null) {
            return Result.error("产品ID不能为空");
        }
        try {
            alertService.saveConfig(config);
            return Result.success();
        } catch (Exception e) {
            return Result.error("保存失败: " + e.getMessage());
        }
    }

    @GetMapping("/config/{productId}")
    public Result<StockAlertConfig> getConfig(@PathVariable Long productId) {
        return Result.success(alertService.getConfig(productId));
    }

    @GetMapping("/list")
    public Result<List<Map<String, Object>>> list() {
        return Result.success(alertService.getAlertList());
    }
}
