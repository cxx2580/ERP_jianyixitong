package com.erp.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InventoryCheck {
    private Long id;
    private String checkNo;
    private Long productId;
    private String productName;
    private Integer bookStock;
    private Integer actualStock;
    private Integer diffQuantity;
    private Integer status;
    private String operator;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
