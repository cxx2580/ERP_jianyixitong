package com.erp.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StockAlertConfig {
    private Long id;
    private Long productId;
    private String productName;
    private Integer minStock;
    private Integer alertEnabled;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
