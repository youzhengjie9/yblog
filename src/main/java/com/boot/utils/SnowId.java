package com.boot.utils;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel("对雪花漂移算法进行二次封装")
public class SnowId implements Serializable {

    private static IdGeneratorOptions options = new IdGeneratorOptions((short) 1); //雪花算法

    static {
        /**
         * 默认为6：50w并发需要8秒
         * 设置为10：50w并发只需要0.5秒，提升巨大
         */
        options.SeqBitLength=10;
        YitIdHelper.setIdGenerator(options);
    }

    //雪花算法生成分布式id
    public static long nextId(){

        return YitIdHelper.nextId();
    }

}
