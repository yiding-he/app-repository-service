package com.hyd.apprepositoryserver.repository;

import lombok.Data;

@Data
public class App {

  private String org;

  private String artifact;

  private String version;

  public App() {
  }

  public App(String org, String artifact, String version) {
    this.org = org;
    this.artifact = artifact;
    this.version = version;
  }

  public String toFQN() {
    return String.join(":", org, artifact, version);
  }
}
