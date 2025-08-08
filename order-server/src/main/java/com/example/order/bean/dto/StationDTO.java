package com.example.order.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class StationDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2569579188268095196L;
    private Long stationId;
    private String stationName;
    private String areaName;
    private String operatorName;


}
