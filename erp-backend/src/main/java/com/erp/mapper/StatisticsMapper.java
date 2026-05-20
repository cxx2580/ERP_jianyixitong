package com.erp.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface StatisticsMapper {
    List<Map<String, Object>> selectSalesTrend(@Param("months") int months);
    List<Map<String, Object>> selectCustomerSalesRanking(@Param("top") int top);
    List<Map<String, Object>> selectOrderStatusCount();
    List<Map<String, Object>> selectProductSalesRanking(@Param("top") int top);
    Integer countCompletedProduction();
    List<Map<String, Object>> selectProductionQuantity();
    List<Map<String, Object>> selectProductionStatusCount();
    List<Map<String, Object>> selectPurchaseTrend(@Param("months") int months);
    List<Map<String, Object>> selectSupplierPurchaseRanking(@Param("top") int top);
    List<Map<String, Object>> selectPurchaseStatusCount();
    List<Map<String, Object>> selectInventoryValue();
    List<Map<String, Object>> selectInventoryTrend(@Param("days") int days);
    List<Map<String, Object>> selectLowStockAlerts();
    Double getMonthlySales();
    Double getInventoryTotalValue();
    Integer countPendingOrders();
    Integer countLowStock();
}
