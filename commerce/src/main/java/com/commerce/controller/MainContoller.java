package com.commerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainContoller {

    @RequestMapping("/main")
    public String home(Model model) {
        return "index";
    }

    @RequestMapping("/cb")
    public String cb(){
        return "cb";
    }
}
