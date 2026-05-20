package com.erp.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InventoryRecord {
    private Long id;
    private Long productId;
    private String productName;
    private String changeType;
    private Integer changeQuantity;
    private Integer beforeStock;
    private Integer afterStock;
    private String relatedOrderNo;
    private String operator;
    private String remark;
    private LocalDateTime createTime;
}
