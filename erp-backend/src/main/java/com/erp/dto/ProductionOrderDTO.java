package com.erp.dto;

import com.erp.entity.ProductionOrder;
import com.erp.entity.ProductionMaterial;
import lombok.Data;
import java.util.List;

@Data
public class ProductionOrderDTO {
    private ProductionOrder order;
    private List<ProductionMaterial> materials;
}
