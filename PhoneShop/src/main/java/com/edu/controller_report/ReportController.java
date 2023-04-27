package com.edu.controller_report;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.edu.reponsitory.OrderDetailReponsitory;
import com.edu.reponsitory.ReportReponsitory;
import com.edu.report.hangnam;
import com.edu.report.hangngay;

@Controller
public class ReportController {
    @Autowired
    ReportReponsitory reponsitory;
    
    @Autowired
    OrderDetailReponsitory orderDetailReponsitory;
    
   
   
   
    
    @RequestMapping("/report")
    public String re1(Model model) {
        Calendar cal = Calendar.getInstance();
        Map<String, Double> surveyMap = new LinkedHashMap<>();
        
        List<hangngay> list = reponsitory.reportngay(Date.from(java.time.LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        for (int i = 0; i < list.size(); i++) {
            surveyMap.put(list.get(i).getName(), list.get(i).getSum());
           
        }
        
        model.addAttribute("surveyMap", surveyMap);
        
        model.addAttribute("report", reponsitory.reportngay(Date.from(java.time.LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));
        model.addAttribute("ngay", java.time.LocalDate.now());
        model.addAttribute("sum", reponsitory.reporttongngay(Date.from(java.time.LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));
        return "report/hn";
    }
    @RequestMapping("/report_month")
    public String month(Model model, @RequestParam(value = "month", defaultValue = "11") int month) {
        Calendar cal = Calendar.getInstance();
        Map<Integer, Double> surveyMap = new LinkedHashMap<>();
      
        List<hangnam> list = reponsitory.reportthang(month);
        for (int i = 0; i < list.size(); i++) {
            surveyMap.put(list.get(i).getDate(), list.get(i).getSum());
           
        }
        model.addAttribute("month", month);
        model.addAttribute("sum", reponsitory.reporttongthang(month));
        model.addAttribute("surveyMap", surveyMap);
        return "report/line";
    }
    
    @RequestMapping("/report_year")
    public String line(Model model, @RequestParam(value = "year", defaultValue = "2022") int year) {
        Calendar cal = Calendar.getInstance();
        Map<Integer, Double> surveyMap = new LinkedHashMap<>();
      
        List<hangnam> list = reponsitory.reportnam(year);
        for (int i = 0; i < list.size(); i++) {
            surveyMap.put(list.get(i).getDate(), list.get(i).getSum());
           
        }
        model.addAttribute("year", year);
        model.addAttribute("sum", reponsitory.reporttongnam(year));
        model.addAttribute("surveyMap", surveyMap);
        return "report/line2";
    }
}
