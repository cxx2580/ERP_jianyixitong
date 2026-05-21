package com.erp.mapper;

import com.erp.entity.Material;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MaterialMapper {
    int insert(Material material);
    int update(Material material);
    int deleteById(Long id);
    Material selectById(Long id);
    List<Material> selectList(@Param("keyword") String keyword,
                               @Param("offset") int offset,
                               @Param("limit") int limit);
    int count(@Param("keyword") String keyword);
}
