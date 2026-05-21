package com.erp.service;

import com.erp.common.PageResult;
import com.erp.entity.Material;
import com.erp.mapper.MaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MaterialService {
    @Autowired
    private MaterialMapper materialMapper;

    public int add(Material material) { return materialMapper.insert(material); }
    public int update(Material material) { return materialMapper.update(material); }
    public int delete(Long id) { return materialMapper.deleteById(id); }
    public Material getById(Long id) { return materialMapper.selectById(id); }

    public PageResult<Material> list(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Material> list = materialMapper.selectList(keyword, offset, pageSize);
        int total = materialMapper.count(keyword);
        return PageResult.of(total, list, page, pageSize);
    }
}
