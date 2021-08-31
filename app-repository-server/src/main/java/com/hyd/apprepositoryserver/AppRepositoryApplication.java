package com.hyd.apprepositoryserver;

import com.hyd.apprepositoryserver.repository.RepositoryConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
@EnableConfigurationProperties({RepositoryConfig.class})
@Slf4j
public class AppRepositoryApplication {

  @Autowired
  private RepositoryConfig repositoryConfig;

  public static void main(String[] args) {
    SpringApplication.run(AppRepositoryApplication.class, args);
  }

  @Bean
  ApplicationRunner applicationRunner() {
    // 尝试初始化 App 库目录，如果失败则抛出异常中止启动
    return args -> {
      Path path = Paths.get(repositoryConfig.getRoot());
      log.info("Application repository location: {}", path.toAbsolutePath());
      if (!Files.exists(path)) {
        Files.createDirectories(path);
      }
    };
  }
}
