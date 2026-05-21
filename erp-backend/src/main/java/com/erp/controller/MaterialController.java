package com.erp.controller;

import com.erp.common.PageResult;
import com.erp.common.Result;
import com.erp.entity.Bom;
import com.erp.entity.Material;
import com.erp.service.BomService;
import com.erp.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/material")
@CrossOrigin
@Validated
public class MaterialController {
    @Autowired
    private MaterialService materialService;
    @Autowired
    private BomService bomService;

    @PostMapping("/add")
    public Result<Void> add(@Valid @RequestBody Material material) {
        materialService.add(material);
        return Result.success();
    }
    @PostMapping("/update")
    public Result<Void> update(@Valid @RequestBody Material material) {
        materialService.update(material);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        materialService.delete(id);
        return Result.success();
    }
    @GetMapping("/get/{id}")
    public Result<Material> getById(@PathVariable Long id) {
        return Result.success(materialService.getById(id));
    }
    @GetMapping("/list")
    public Result<PageResult<Material>> list(@RequestParam(required = false) String keyword,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(materialService.list(keyword, page, pageSize));
    }

    @GetMapping("/bom/{productId}")
    public Result<List<Bom>> getBom(@PathVariable Long productId) {
        return Result.success(bomService.getByProductId(productId));
    }
    @PostMapping("/bom/save")
    public Result<Void> saveBom(@RequestBody Map<String, Object> body) {
        Long productId = Long.valueOf(body.get("productId").toString());
        List<Bom> boms = null;
        if (body.get("boms") != null) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) body.get("boms");
            boms = list.stream().map(m -> {
                Bom b = new Bom();
                b.setMaterialId(Long.valueOf(m.get("materialId").toString()));
                b.setQuantity(Integer.valueOf(m.get("quantity").toString()));
                return b;
            }).collect(java.util.stream.Collectors.toList());
        }
        bomService.saveBom(productId, boms);
        return Result.success();
    }
}
