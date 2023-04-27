package com.edu.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.reponsitory.ReportReponsitory;
import com.edu.report.hangnam;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/reports")
public class ReportRestController {
    @Autowired
    ReportReponsitory reponsitory;
    
    @GetMapping()
    public List<hangnam> getAll(@RequestParam(value = "month", defaultValue = "11") int month) {
        return reponsitory.reportthang(month);
    }
}
