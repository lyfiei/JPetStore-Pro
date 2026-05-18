package com.csu.jpetstore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

// 只加这一行 @MapperScan ！！！
@MapperScan("com.csu.jpetstore.mapper")
@SpringBootApplication
@ServletComponentScan("com.csu.jpetstore.servlet")
public class JPetStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(JPetStoreApplication.class, args);
    }
}