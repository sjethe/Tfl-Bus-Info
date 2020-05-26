package com.example.tflbusinfo.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;

@Data
@Getter
@Setter
public class StopPoint implements Serializable {

    private String id;
    private String query;
    private String lineId;
    private String lineName;
    private String platformName;
    private String direction;
    private String stationName;
    private String destinationName;
    private String towards;
    private String modeName;
    private Date currentTime;
    private int timeToStation;
    private Date expectedArrival;
    private String timeToLive;
}
