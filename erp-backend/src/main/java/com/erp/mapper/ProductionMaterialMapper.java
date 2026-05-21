package com.erp.mapper;

import com.erp.entity.ProductionMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ProductionMaterialMapper {
    int insert(ProductionMaterial pm);
    int deleteByProductionId(@Param("productionId") Long productionId);
    List<ProductionMaterial> selectByProductionId(@Param("productionId") Long productionId);
}
