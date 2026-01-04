package com.example.demo.service.impl;

import com.example.demo.bean.dto.WatermarkDTO;
import com.example.demo.bean.form.WatermarkQueryForm;
import com.example.demo.bean.form.WatermarkSaveForm;
import com.example.demo.bean.form.WatermarkUpdateForm;
import com.example.demo.bean.po.WatermarkPO;
import com.example.demo.repository.WatermarkRepository;
import com.example.demo.service.WatermarkService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WatermarkServiceImpl implements WatermarkService {

    private final WatermarkRepository watermarkRepository;

    @Override
    public void save(WatermarkSaveForm form) {
        WatermarkPO po = new WatermarkPO();
        BeanUtils.copyProperties(form, po);
        watermarkRepository.save(po);
    }

    @Override
    public void update(WatermarkUpdateForm form) {
        WatermarkPO po = watermarkRepository.findById(form.getId())
                .orElseThrow(() -> new RuntimeException("Watermark not found"));
        BeanUtils.copyProperties(form, po);
        watermarkRepository.save(po);
    }

    @Override
    public void delete(Long id) {
        watermarkRepository.deleteById(id);
    }

    @Override
    public Page<WatermarkDTO> list(WatermarkQueryForm form, Pageable pageable) {
        Specification<WatermarkPO> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(form.getStyle())) {
                predicates.add(cb.equal(root.get("style"), form.getStyle()));
            }
            if (StringUtils.hasText(form.getName())) {
                predicates.add(cb.like(root.get("name"), "%" + form.getName() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<WatermarkPO> page = watermarkRepository.findAll(spec, pageable);
        return page.map(po -> {
            WatermarkDTO dto = new WatermarkDTO();
            BeanUtils.copyProperties(po, dto);
            return dto;
        });
    }

    @Override
    public WatermarkDTO detail(Long id) {
        WatermarkPO po = watermarkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Watermark not found"));
        WatermarkDTO dto = new WatermarkDTO();
        BeanUtils.copyProperties(po, dto);
        return dto;
    }
}
