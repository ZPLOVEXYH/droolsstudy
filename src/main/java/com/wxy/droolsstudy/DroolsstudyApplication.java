package com.wxy.droolsstudy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wxy.droolsstudy.dao")
public class DroolsstudyApplication {
    public static void main(String[] args) {
        SpringApplication.run(DroolsstudyApplication.class, args);
    }
}
