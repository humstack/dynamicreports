package com.controller;

import com.config.SubDatasource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    //private Report report;

    //private DBReport dbReport;

    private SubDatasource subDatasource;

    @GetMapping("/")
    public String index(){

        return "index";
    }

    @PostMapping("/showReport")
    public void showReport(){

        //dbReport = new DBReport();

        if (subDatasource == null){
            subDatasource = new SubDatasource();
        }

    }
}
