package com.hyd.apprepositoryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        RepositoryConfig.class
})
public class AppRepositoryServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppRepositoryServerApplication.class, args);
    }
}
