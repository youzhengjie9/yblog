package com.boot;

import com.boot.config.scanClassProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.TimeZone;

@SpringBootApplication
@EnableSwagger2 //开启swagger2
@EnableConfigurationProperties(value = {scanClassProperties.class,}) //开启自定义配置类
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60*60*3) //开启springSession+redis解决分布式session问题
public class DemoApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(DemoApplication.class, args);

    }


}
