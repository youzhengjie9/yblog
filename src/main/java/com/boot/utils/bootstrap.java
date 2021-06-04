package com.boot.utils;

import java.util.Random;

public final class bootstrap {

    private static final String[] color={"primary","info","success","warning","danger"};

    /**
     * 产生随机颜色
     */
    public static final String randomColor(){

        Random random = new Random();
        int i = random.nextInt(color.length);

        return color[i];
    }


}
