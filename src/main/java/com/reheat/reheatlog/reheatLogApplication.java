package com.reheat.reheatlog;

import com.reheat.reheatlog.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class ReheatLogApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReheatLogApplication.class, args);
    }

}
