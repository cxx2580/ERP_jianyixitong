package com.erp.entity;

import lombok.Data;

@Data
public class Bom {
    private Long id;
    private Long productId;
    private Long materialId;
    private String materialName;
    private String specification;
    private String unit;
    private java.math.BigDecimal price;
    private Integer quantity;
    private java.time.LocalDateTime createTime;
}
