package com.example.tflbusinfo.controller;

import com.example.tflbusinfo.service.TflBusService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Controller
@RequestMapping("/tflbus/V1")
public class TflBusController {

    private final static Logger logger = LogManager.getLogger(TflBusController.class);

    @Autowired
    TflBusService tflBusService;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @GetMapping("/")
    public String getIndexPage(){
        return "index";
    }

    @GetMapping("/busstopinfo")
    public String getArrivalTimesAtGivenStop(@RequestParam("stoppoint") String stopPoint, Model model){
        try{
        List busInfoList = tflBusService.getArrivalTimesAtGivenStop(stopPoint);
        model.addAttribute("businfos", busInfoList);
        }catch(Exception e){
            logger.info("exception :"+e.toString());
            model.addAttribute("businfos",null);
        }
        return "bus-info";
    }
}
