package com.example.material_system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.material_system.mapper")
public class MaterialSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaterialSystemApplication.class, args);
    }

}
