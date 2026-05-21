package com.erp.mapper;

import com.erp.entity.Bom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface BomMapper {
    int insert(Bom bom);
    int update(Bom bom);
    int deleteById(Long id);
    List<Bom> selectByProductId(@Param("productId") Long productId);
    List<Bom> selectAll();
    int deleteByProductId(@Param("productId") Long productId);
}
