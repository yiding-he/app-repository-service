package com.hyd.apprepositoryserver.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RepositoryService {

  @Autowired
  private RepositoryConfig repositoryConfig;

  private static Stream<Path> subFoldersOf(Path parent) {
    try {
      return Files.list(parent).filter(Files::isDirectory);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<App> allApps() {
    Path repoRoot = Paths.get(repositoryConfig.getRoot(), "apps");
    List<App> appList = new ArrayList<>();

    subFoldersOf(repoRoot).forEach(
      orgPath -> subFoldersOf(orgPath).forEach(
        artifactPath -> subFoldersOf(artifactPath).forEach(
          versionPath -> appList.add(new App(
            orgPath.getFileName().toString(),
            artifactPath.getFileName().toString(),
            versionPath.getFileName().toString()
          ))
        )
      )
    );

    return appList;
  }

  public List<App> queryApps(String keyword) {
    return StringUtils.hasText(keyword) ?
      allApps().stream()
        .filter(app -> app.toFQN().contains(keyword))
        .collect(Collectors.toList())
      : allApps()
      ;
  }

  public void saveApp(String type, String fqn, MultipartFile[] files) throws IOException {
    if (type.equals("spring-boot")) {
      saveSpringBootApp(fqn, files);
    }
  }

  private void saveSpringBootApp(String fqn, MultipartFile[] files) throws IOException {
    Path directory = Paths.get(repositoryConfig.getRoot(), "apps", fqn.replace(":", "/"));
    for (MultipartFile file : files) {
      if (StringUtils.hasText(file.getOriginalFilename())) {
        file.transferTo(directory.resolve(file.getOriginalFilename()));
      }
    }
  }
}
