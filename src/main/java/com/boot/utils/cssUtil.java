package com.boot.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class cssUtil {

    private static final String color[]={"darkviolet","#1E9FFF","#4bad4b","#6c757d","#28a745","pink","orange"};



    public String randomCssColor(){
        Random random = new Random();
        int i = random.nextInt(color.length);
        return color[i];
    }



}
