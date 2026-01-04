package com.example.demo.controller;

import com.example.demo.bean.dto.WatermarkDTO;
import com.example.demo.bean.form.WatermarkQueryForm;
import com.example.demo.bean.form.WatermarkSaveForm;
import com.example.demo.bean.form.WatermarkUpdateForm;
import com.example.demo.service.WatermarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/watermark")
@RequiredArgsConstructor
public class WatermarkController {

    private final WatermarkService watermarkService;

    @PostMapping("/save")
    public void save(@RequestBody WatermarkSaveForm form) {
        watermarkService.save(form);
    }

    @PostMapping("/update")
    public void update(@RequestBody WatermarkUpdateForm form) {
        watermarkService.update(form);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        watermarkService.delete(id);
    }

    @GetMapping("/list")
    public Page<WatermarkDTO> list(
            WatermarkQueryForm form,
            @PageableDefault(size = 20, sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return watermarkService.list(form, pageable);
    }

    @GetMapping("/{id}")
    public WatermarkDTO detail(@PathVariable Long id) {
        return watermarkService.detail(id);
    }
}
