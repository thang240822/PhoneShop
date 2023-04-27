package com.edu.controller_report;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeReport {
    @RequestMapping("/report/home")
    public String home(Model model) {
        return "report/layout";
    }
}
