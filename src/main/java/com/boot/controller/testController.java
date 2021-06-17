package com.boot.controller;

import com.boot.annotation.Visitor;
import com.boot.data.ResponseData.ResponseJSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/test")
public class testController {



    @Visitor()
    @GetMapping(path = "/t")
    public ResponseJSON responseJSON(){
        System.out.println("================1");

        ResponseJSON responseJSON = new ResponseJSON();
        responseJSON.setData("test---");
        responseJSON.setResult(200);
        responseJSON.setDt(true);
        System.out.println("===================2");
        return responseJSON;
    }







}
