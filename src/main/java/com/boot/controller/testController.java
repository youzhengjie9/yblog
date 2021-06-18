package com.boot.controller;

import com.boot.annotation.Visitor;
import com.boot.data.ResponseData.ResponseJSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/test")
public class testController {



    @Visitor(desc = "test...")
    @GetMapping(path = "/t")
    public ResponseJSON responseJSON(@Value("test...") String desc){



        System.out.println("================1");

        ResponseJSON responseJSON = new ResponseJSON();
        responseJSON.setData("test---");
        responseJSON.setResult(200);
        responseJSON.setDt(true);
        System.out.println("===================2");
        return responseJSON;
    }







}
