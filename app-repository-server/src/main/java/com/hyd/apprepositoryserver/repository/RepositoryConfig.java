package com.hyd.apprepositoryserver.repository;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("repository")
@Data
public class RepositoryConfig {

    private String root;

}
