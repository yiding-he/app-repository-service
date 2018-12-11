package com.hyd.apprepositoryserver;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("repository")
public class RepositoryConfig {

    private String root;

    private String urlPrefix;

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }
}
