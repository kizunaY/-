package com.mvc.study.controller;

import com.mvc.study.annotation.Controller;
import com.mvc.study.annotation.RequestMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
//    @RequestMapper("/")
public class MyController {

    @RequestMapper("/index")
    public void index(HttpServletRequest request, HttpServletResponse response){
        try {
            response.getOutputStream().print("welcome page");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapper("/")
    public void login(HttpServletRequest request, HttpServletResponse response){
        try {
            response.getOutputStream().print("welcome");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
