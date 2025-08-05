package com.example.order.bean.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class StationDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2569579188268095196L;
    private Long stationId;
    private String stationName;
    private String areaName;
    private String operatorName;


    public StationDTO(Long stationId, String stationName, String areaName, String operatorName) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.areaName = areaName;
        this.operatorName = operatorName;
    }
//
//    public Long getStationId() {
//        return stationId;
//    }
//
//    public void setStationId(Long stationId) {
//        this.stationId = stationId;
//    }
//
//    public String getStationName() {
//        return stationName;
//    }
//
//    public void setStationName(String stationName) {
//        this.stationName = stationName;
//    }
//
//    public String getAreaName() {
//        return areaName;
//    }
//
//    public void setAreaName(String areaName) {
//        this.areaName = areaName;
//    }
//
//    public String getOperatorName() {
//        return operatorName;
//    }
//
//    public void setOperatorName(String operatorName) {
//        this.operatorName = operatorName;
//    }
}
