package com.erp.mapper;

import com.erp.entity.InventoryCheck;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface InventoryCheckMapper {
    int insert(InventoryCheck check);
    int update(InventoryCheck check);
    int deleteById(Long id);
    InventoryCheck selectById(Long id);
    List<InventoryCheck> selectList(@Param("checkNo") String checkNo,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit);
    int count(@Param("checkNo") String checkNo);
}
