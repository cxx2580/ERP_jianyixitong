package com.erp.service;

import com.erp.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {
    @Autowired
    private StatisticsMapper statisticsMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductionOrderMapper productionOrderMapper;

    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> result = new HashMap<>();
        result.put("customerCount", customerMapper.count(null));
        result.put("productCount", productMapper.count(null));
        result.put("monthlySales", statisticsMapper.getMonthlySales());
        result.put("inventoryValue", statisticsMapper.getInventoryTotalValue());
        result.put("pendingOrders", statisticsMapper.countPendingOrders());
        result.put("lowStockAlerts", statisticsMapper.countLowStock());
        result.put("salesTrend", statisticsMapper.selectSalesTrend(12));
        return result;
    }

    public Map<String, Object> getSalesStats(String type) {
        Map<String, Object> result = new HashMap<>();
        switch (type != null ? type : "") {
            case "trend": result.put("data", statisticsMapper.selectSalesTrend(12)); break;
            case "ranking": result.put("data", statisticsMapper.selectCustomerSalesRanking(10)); break;
            case "status": result.put("data", statisticsMapper.selectOrderStatusCount()); break;
            case "productRanking": result.put("data", statisticsMapper.selectProductSalesRanking(10)); break;
            default:
                result.put("trend", statisticsMapper.selectSalesTrend(12));
                result.put("ranking", statisticsMapper.selectCustomerSalesRanking(10));
                result.put("status", statisticsMapper.selectOrderStatusCount());
        }
        return result;
    }

    public Map<String, Object> getProductionStats(String type) {
        Map<String, Object> result = new HashMap<>();
        switch (type != null ? type : "") {
            case "rate":
                int total = productionOrderMapper.count(null);
                int completed = statisticsMapper.countCompletedProduction();
                result.put("total", total);
                result.put("completed", completed);
                result.put("rate", total == 0 ? 0 : Math.round((double) completed / total * 10000.0) / 100.0);
                break;
            case "quantity": result.put("data", statisticsMapper.selectProductionQuantity()); break;
            case "status": result.put("data", statisticsMapper.selectProductionStatusCount()); break;
            default:
                result.put("quantity", statisticsMapper.selectProductionQuantity());
                result.put("status", statisticsMapper.selectProductionStatusCount());
        }
        return result;
    }

    public Map<String, Object> getPurchaseStats(String type) {
        Map<String, Object> result = new HashMap<>();
        switch (type != null ? type : "") {
            case "trend": result.put("data", statisticsMapper.selectPurchaseTrend(12)); break;
            case "ranking": result.put("data", statisticsMapper.selectSupplierPurchaseRanking(10)); break;
            case "status": result.put("data", statisticsMapper.selectPurchaseStatusCount()); break;
            default:
                result.put("trend", statisticsMapper.selectPurchaseTrend(12));
                result.put("ranking", statisticsMapper.selectSupplierPurchaseRanking(10));
                result.put("status", statisticsMapper.selectPurchaseStatusCount());
        }
        return result;
    }

    public Map<String, Object> getInventoryStats(String type) {
        Map<String, Object> result = new HashMap<>();
        switch (type != null ? type : "") {
            case "value": result.put("data", statisticsMapper.selectInventoryValue()); break;
            case "trend": result.put("data", statisticsMapper.selectInventoryTrend(30)); break;
            case "alert": result.put("data", statisticsMapper.selectLowStockAlerts()); break;
            default:
                result.put("value", statisticsMapper.selectInventoryValue());
                result.put("alert", statisticsMapper.selectLowStockAlerts());
        }
        return result;
    }
}
