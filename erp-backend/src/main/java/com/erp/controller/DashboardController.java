package com.erp.controller;

import com.erp.common.Result;
import com.erp.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin
@Validated
public class DashboardController {
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/summary")
    public Result<Map<String, Object>> summary() {
        return Result.success(statisticsService.getDashboardSummary());
    }
}
