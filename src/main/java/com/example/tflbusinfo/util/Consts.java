package com.example.tflbusinfo.util;

public final class Consts {

    private Consts(){
    }

    public static final String BASE_PATH = "https://api.tfl.gov.uk/";
    public static final String BASE_POINT_PATH = BASE_PATH + "StopPoint/";
    public static final String SEARCH_PATH = BASE_POINT_PATH + "Search/";
    public static final String MODE_PATH = "/Arrivals?mode=bus";

    public static final String NO_DATA_FOUND = "No  Data Found for the request";
}
