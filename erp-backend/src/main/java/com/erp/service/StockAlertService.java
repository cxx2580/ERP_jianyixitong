package com.erp.service;

import com.erp.entity.Product;
import com.erp.entity.StockAlertConfig;
import com.erp.mapper.ProductMapper;
import com.erp.mapper.StockAlertConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockAlertService {
    @Autowired
    private StockAlertConfigMapper configMapper;
    @Autowired
    private ProductMapper productMapper;

    public int saveConfig(StockAlertConfig config) {
        StockAlertConfig exist = configMapper.selectByProductId(config.getProductId());
        if (exist != null) {
            config.setId(exist.getId());
            return configMapper.update(config);
        }
        Product product = productMapper.selectById(config.getProductId());
        if (product != null) {
            config.setProductName(product.getProductName());
        }
        return configMapper.insert(config);
    }

    public StockAlertConfig getConfig(Long productId) {
        return configMapper.selectByProductId(productId);
    }

    public List<Map<String, Object>> getAlertList() {
        List<Map<String, Object>> alerts = new ArrayList<>();
        List<StockAlertConfig> configs = configMapper.selectEnabled();
        for (StockAlertConfig cfg : configs) {
            Product p = productMapper.selectById(cfg.getProductId());
            if (p != null && p.getStock() < cfg.getMinStock()) {
                Map<String, Object> item = new HashMap<>();
                item.put("productId", p.getId());
                item.put("productName", p.getProductName());
                item.put("currentStock", p.getStock());
                item.put("minStock", cfg.getMinStock());
                item.put("gap", cfg.getMinStock() - p.getStock());
                alerts.add(item);
            }
        }
        return alerts;
    }
}
