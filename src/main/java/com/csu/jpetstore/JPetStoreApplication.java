package com.csu.jpetstore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 只加这一行 @MapperScan ！！！
@MapperScan("com.csu.jpetstore.mapper")
@SpringBootApplication
public class JPetStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(JPetStoreApplication.class, args);
    }
}