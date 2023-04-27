package com.edu.controller_report;

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

import com.edu.reponsitory.UserReponsitory;
import com.edu.report.hangngay;
import com.edu.report.user;

@Controller
public class UserReport {
    @Autowired
    UserReponsitory userReponsitory;
    
    @RequestMapping("/user/report")
    public String report(Model model) {
        Calendar cal = Calendar.getInstance();
        Map<String, Long> surveyMap = new LinkedHashMap<>();
        List<user> list = userReponsitory.load();
        for (int i = 0; i < list.size(); i++) {
            surveyMap.put(list.get(i).getFullname(), list.get(i).getCount());
           
        }
        
        model.addAttribute("surveyMap", surveyMap);
        
        model.addAttribute("report", userReponsitory.load());
       
        return "report/user_report";
    }
}
