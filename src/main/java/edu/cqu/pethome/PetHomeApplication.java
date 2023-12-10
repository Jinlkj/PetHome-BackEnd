package edu.cqu.pethome;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("edu.cqu.pethome.mapper")
@SpringBootApplication
public class PetHomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetHomeApplication.class, args);
    }

}
