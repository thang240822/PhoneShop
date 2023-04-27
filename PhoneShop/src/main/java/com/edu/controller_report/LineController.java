package com.edu.controller_report;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.edu.model.OrderDetail;
import com.edu.model.Product;
import com.edu.reponsitory.OrderDetailReponsitory;
import com.edu.reponsitory.ReportReponsitory;
import com.edu.report.hangnam;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LineController {
    @Autowired
    OrderDetailReponsitory orderDetailReponsitory;
    
    @Autowired
    ReportReponsitory reponsitory;
   
}
