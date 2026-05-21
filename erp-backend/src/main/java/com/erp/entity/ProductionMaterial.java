package com.erp.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductionMaterial {
    private Long id;
    private Long productionId;
    private Long materialId;
    private String materialName;
    private String specification;
    private String unit;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}
