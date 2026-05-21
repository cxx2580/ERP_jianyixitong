package com.erp.entity;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProductionOrder {
    private Long id;
    @NotBlank(message = "生产单号不能为空")
    private String productionNo;
    @NotNull(message = "产品不能为空")
    private Long productId;
    private String productName;
    private String specification;
    private String unit;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualEndDate;
    private Integer status;
    private String responsiblePerson;
    private String remark;
    private Long salesOrderId;
    private String salesOrderNo;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
