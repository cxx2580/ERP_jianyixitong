package com.erp.controller;

import com.erp.common.Result;
import com.erp.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin
@Validated
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/sales")
    public Result<Map<String, Object>> salesStats(@RequestParam(defaultValue = "") String type) {
        return Result.success(statisticsService.getSalesStats(type));
    }

    @GetMapping("/production")
    public Result<Map<String, Object>> productionStats(@RequestParam(defaultValue = "") String type) {
        return Result.success(statisticsService.getProductionStats(type));
    }

    @GetMapping("/purchase")
    public Result<Map<String, Object>> purchaseStats(@RequestParam(defaultValue = "") String type) {
        return Result.success(statisticsService.getPurchaseStats(type));
    }

    @GetMapping("/inventory")
    public Result<Map<String, Object>> inventoryStats(@RequestParam(defaultValue = "") String type) {
        return Result.success(statisticsService.getInventoryStats(type));
    }
}
