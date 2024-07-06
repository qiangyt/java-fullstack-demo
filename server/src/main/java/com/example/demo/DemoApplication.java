package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.demo.server.dao.MessageDao;
import com.example.demo.server.entity.UserEntity;

import io.github.qiangyt.common.rest.RestConfig;
import io.github.qiangyt.common.security.SecurityConfig;

@SpringBootApplication(scanBasePackages = { "com.example.demo", "io.github.qiangyt.common" })
@Import({ RestConfig.class, SecurityConfig.class })
@EnableJpaAuditing
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
