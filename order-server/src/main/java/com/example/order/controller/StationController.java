package com.example.order.controller;

import com.example.order.bean.dto.StationDTO;
import com.example.order.service.StationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/station/controller")
@Tag(name = "StationController",description = "用于测试 Criteria API + DTO 实现联表查询")
public class StationController {


    @Autowired
    StationService stationService;

    @GetMapping("/stations")
    public PagedModel<StationDTO> getStations(
            @RequestParam(required = false) String nameFilter,
            @ParameterObject Pageable pageable
    ) {

        /// 在实际使用过程中应该使用 PagedModel 而非 Page
        /// PagedModel<T> 是你自定义的轻量分页返回体，适合 DTO 开发。
        /// 避免了直接暴露 JPA Page<T>，结构更清晰，也更容易前端对接。
        /// 可根据实际情况拓展 _links、extra 字段等。
        return new PagedModel<>(stationService.queryStations(nameFilter, pageable));
    }
}
