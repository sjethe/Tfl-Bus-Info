package com.example.tflbusinfo.service;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface TflBusService {

    List getArrivalTimesAtGivenStop(String stopName);
    String getBusStopByName(String stopName);
    List getStopPoints(String parent);

}
