package com.example.demo.service;

import com.example.demo.bean.dto.WatermarkDTO;
import com.example.demo.bean.form.WatermarkQueryForm;
import com.example.demo.bean.form.WatermarkSaveForm;
import com.example.demo.bean.form.WatermarkUpdateForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WatermarkService {
    void save(WatermarkSaveForm form);

    void update(WatermarkUpdateForm form);

    void delete(Long id);

    Page<WatermarkDTO> list(WatermarkQueryForm form, Pageable pageable);

    WatermarkDTO detail(Long id);
}
