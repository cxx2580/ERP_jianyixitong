package com.erp.mapper;

import com.erp.entity.InventoryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface InventoryRecordMapper {
    int insert(InventoryRecord record);
    InventoryRecord selectById(Long id);
    List<InventoryRecord> selectList(@Param("changeType") String changeType,
                                      @Param("keyword") String keyword,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);
    int count(@Param("changeType") String changeType,
              @Param("keyword") String keyword);
    List<InventoryRecord> selectByProductId(@Param("productId") Long productId,
                                             @Param("offset") int offset,
                                             @Param("limit") int limit);
}
