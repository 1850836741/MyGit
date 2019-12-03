package com.example.business_server.controller;

import com.example.business_server.entity.SourceOrder;
import com.example.business_server.entity.SourceOrderInformation;
import com.example.business_server.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SourceController {

    @Autowired
    SourceService sourceService;

    @RequestMapping("")
    public String findSourceById(Model model,String source_order_id){
        model.addAttribute("source",sourceService.findSourceById(source_order_id));
        model.addAttribute("source",sourceService.findSourceInformationById(source_order_id));
        return "";
    }

    @RequestMapping
    public String findSource(Model model){
        model.addAttribute("sourceList",sourceService.findSourceOrderLimitNumber(50));
        return "";
    }

    @RequestMapping
    public String deleteSource(String source_order_id){
        sourceService.deleteSource(source_order_id);
        return "";
    }

    @RequestMapping
    public String addSource(Model model, SourceOrder sourceOrder){
        sourceService.addSource(sourceOrder);
        return "";
    }

    @RequestMapping
    public String  addSourceInformation(SourceOrderInformation sourceOrderInformation){
        sourceService.findSourceById(sourceOrderInformation.getSource_order_id());
        sourceService.addSourceInformation(sourceOrderInformation);
        return "";
    }
}
