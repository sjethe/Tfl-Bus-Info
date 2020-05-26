package com.example.tflbusinfo.service;

import com.example.tflbusinfo.exception.TflBusInfoException;
import com.example.tflbusinfo.model.StopPoint;
import com.example.tflbusinfo.util.Consts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TflBusServiceImplTest {

    @Autowired
    TflBusService tflBusService = new TflBusServiceImpl();

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test(expected = TflBusInfoException.class)
    public void getArrivalTimesAtGivenStop_shouldThrowException() {
        String stopPointJson = "{'$type':'Tfl.Api.Presentation.Entities.SearchResponse, Tfl.Api.Presentation.Entities','query':'great portland street','matches':[{'topMostParentId':'940GZZLUGPS','modes':['tube','bus'],'zone':'1','name':'Great Portland Street Underground Station'}]}";
        String stopName = "great portland street";
        mockServer.expect(requestTo(Consts.SEARCH_PATH + "great%20portland%20street")).andRespond(withSuccess(stopPointJson, MediaType.TEXT_PLAIN));
        List<StopPoint> stopPoints = tflBusService.getArrivalTimesAtGivenStop(stopName);
    }

    @Test(expected = TflBusInfoException.class)
    public void getStopPoints_shouldThrowException(){
        String stopPointJson = "{'$type':'Tfl.Api.Presentation.Entities.SearchResponse, Tfl.Api.Presentation.Entities','query':'great portland street','matches':[{'topMostParentId':'940GZZLUGPS','modes':['tube','bus'],'zone':'1','name':'Great Portland Street Underground Station'}]}";
        String parentId = "940GZZLUGPS";
        mockServer.expect(requestTo(Consts.BASE_POINT_PATH + parentId)).andRespond(withSuccess(stopPointJson, MediaType.TEXT_PLAIN));
        List stopPoints = tflBusService.getStopPoints(parentId);
    }

    @Test
    public void getBusStopByName_shouldReturnSuccess() {
        String stopPointJson = "{'$type':'Tfl.Api.Presentation.Entities.SearchResponse, Tfl.Api.Presentation.Entities','query':'great portland street','matches':[{'topMostParentId':'940GZZLUGPS','modes':['tube','bus'],'zone':'1','id':'940GZZLUGPS','name':'Great Portland Street Underground Station'}]}";
        String stopName = "great portland street";
        String expectedBusStopId = "940GZZLUGPS";
        mockServer.expect(requestTo(Consts.SEARCH_PATH + "great%20portland%20street")).andRespond(withSuccess(stopPointJson, MediaType.TEXT_PLAIN));
        String actualBusStopId = tflBusService.getBusStopByName(stopName);
        assertEquals(expectedBusStopId, actualBusStopId);
    }

    @Test(expected = TflBusInfoException.class)
    public void getBusStopByName_shouldThrowException() {
        String stopPointJson = "{'$type':'Tfl.Api.Presentation.Entities.SearchResponse, Tfl.Api.Presentation.Entities','query':'great portland street','matches':[{'topMostParentId':'940GZZLUGPS','modes':['tube','bus'],'zone':'1','name':'Great Portland Street Underground Station'}]}";
        String stopName = "great portland street";
        mockServer.expect(requestTo(Consts.SEARCH_PATH + "great%20portland%20street")).andRespond(withSuccess(stopPointJson, MediaType.TEXT_PLAIN));
        String actualBusStopId = tflBusService.getBusStopByName(stopName);
    }
}
