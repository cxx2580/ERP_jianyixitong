package com.erp.mapper;

import com.erp.entity.StockAlertConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StockAlertConfigMapper {
    int insert(StockAlertConfig config);
    int update(StockAlertConfig config);
    StockAlertConfig selectByProductId(@Param("productId") Long productId);
    List<StockAlertConfig> selectAll();
    List<StockAlertConfig> selectEnabled();
}
