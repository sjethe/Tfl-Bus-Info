package com.example.tflbusinfo.service;

import com.example.tflbusinfo.exception.TflBusInfoException;
import com.example.tflbusinfo.model.StopPoint;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

import com.example.tflbusinfo.util.Consts;

@Service
public class TflBusServiceImpl implements TflBusService{

    private final static Logger logger = LogManager.getLogger(TflBusServiceImpl.class);

    @Autowired
    RestTemplate restTemplate;

    /**
     *
     * @param stopName a Stop Name to find bus details
     * @return returns list of StopPoint objects with details
     */
    @Override
    public List<StopPoint> getArrivalTimesAtGivenStop(String stopName) {
        try {
            String naptanId = getBusStopByName(stopName);
            List<String> stopPoints = getStopPoints(naptanId);
            List<StopPoint> stopPointsList = new ArrayList<>();
            StopPoint[] stopPointsArr;
            if(stopPoints != null && stopPoints.size() > 0) {
                for (String stopPoint : stopPoints) {
                    stopPointsArr = restTemplate.getForObject(Consts.BASE_POINT_PATH + stopPoint + Consts.MODE_PATH, StopPoint[].class);
                    stopPointsList.addAll(Arrays.asList(stopPointsArr));
                }
                Collections.sort(stopPointsList, new Comparator<StopPoint>() {
                    @Override
                    public int compare(StopPoint o1, StopPoint o2) {
                        if (o1.getExpectedArrival().before(o2.getExpectedArrival())) {
                            return -1;
                        } else if (o1.getExpectedArrival().after(o2.getExpectedArrival())) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
            }
            return stopPointsList;
        }catch(Exception e){
            logger.error("exception : "+e.toString());
            throw new TflBusInfoException("No arrival timings found");
        }
    }

    /**
     *
     * @param stopName a Stop Name to find bus details
     * @return an id of stopName
     */
    @Override
    public String getBusStopByName(String stopName){
        try {
            String stopPoint = restTemplate.getForObject(Consts.SEARCH_PATH + stopName, String.class);
            JsonParser jsonParser = new JsonParser();
            JsonElement rootNode = jsonParser.parse(stopPoint);
            JsonObject details = rootNode.getAsJsonObject();
            return (details.get("matches").getAsJsonArray().get(0)).getAsJsonObject().get("id").getAsString();
        }catch(Exception e){
            logger.error("exception : "+e.toString());
            throw new TflBusInfoException("Bus stop not found");
        }
    }

    /**
     *
     * @param parentId a id of main stop name
     * @return a list of stop points having platform name 'G' and 'H' at main stop
     */
    @Override
    public List getStopPoints(String parentId) {

        try {
        List<String> stopLists = new ArrayList<>();
        String jsonStopPoints = restTemplate.getForObject(Consts.BASE_POINT_PATH + parentId, String.class);
        JsonParser jsonParser = new JsonParser();
        JsonElement rootNode = jsonParser.parse(jsonStopPoints);
        JsonObject details = rootNode.getAsJsonObject();
        Iterator iterator = details.get("children").getAsJsonArray().iterator();
        while(iterator.hasNext()){
            JsonObject innerObject = (JsonObject)iterator.next();
            if((innerObject).get("children").getAsJsonArray().size()>0){
                Iterator innerIterator = (innerObject).get("children").getAsJsonArray().iterator();
                while(innerIterator.hasNext()){
                    JsonObject innerChildernObject = (JsonObject)innerIterator.next();
                    if((innerChildernObject).get("stopLetter") != null) {
                        if ((innerChildernObject).get("stopLetter").getAsString().equalsIgnoreCase("G") || (innerChildernObject).get("stopLetter").getAsString().equalsIgnoreCase("H")) {
                            stopLists.add((innerChildernObject).get("naptanId").getAsString());
                        }
                    }
                }
            }
        }
        return stopLists;
        }catch (Exception e){
            logger.error("exception : "+e.toString());
            throw new TflBusInfoException("Stop points not found");
        }

    }
}
