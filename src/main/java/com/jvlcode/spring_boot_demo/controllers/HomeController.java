package com.jvlcode.spring_boot_demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class HomeController {

    @GetMapping
    public String getHomePage(){
        return "Welcome to HomePage";
    }
    @GetMapping("/dashboard")
    public String getDashboardPage(){
        return "login Succesfull";
    }


}
