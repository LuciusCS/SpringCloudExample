package com.example.order.service;

import com.example.order.bean.dto.StationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StationService {

     Page<StationDTO> queryStations(String nameFilter, Pageable pageable);
}
