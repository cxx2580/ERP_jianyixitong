package com.erp.service;

import com.erp.entity.Bom;
import com.erp.entity.Material;
import com.erp.mapper.BomMapper;
import com.erp.mapper.MaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BomService {
    @Autowired
    private BomMapper bomMapper;
    @Autowired
    private MaterialMapper materialMapper;

    public List<Bom> getByProductId(Long productId) {
        return bomMapper.selectByProductId(productId);
    }

    @Transactional
    public int saveBom(Long productId, List<Bom> boms) {
        bomMapper.deleteByProductId(productId);
        int count = 0;
        if (boms != null) {
            for (Bom bom : boms) {
                bom.setProductId(productId);
                Material m = materialMapper.selectById(bom.getMaterialId());
                if (m != null) {
                    bom.setMaterialName(m.getMaterialName());
                    bom.setSpecification(m.getSpecification());
                    bom.setUnit(m.getUnit());
                    bom.setPrice(m.getPrice());
                }
                count += bomMapper.insert(bom);
            }
        }
        return count;
    }
}
