package com.hyd.apprepositoryserver;

import com.hyd.apprepositoryserver.repository.App;
import com.hyd.apprepositoryserver.repository.RepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class AppController {

  @Autowired
  private RepositoryService repositoryService;

  @GetMapping("/")
  public String index(@RequestParam(required = false, name = "keyword") String keyword) {

    List<App> appList = repositoryService.queryApps(keyword);
    appList.sort(Comparator.comparing(App::toFQN));

    return String.join("\n",
      "=== All Applications ===",
      appList.stream().map(App::toFQN).collect(Collectors.joining("\n"))
    );
  }

  @PostMapping("/upload/{type}/{fqn}")
  public String upload(
    @PathVariable String type, @PathVariable String fqn, @RequestParam("files") MultipartFile[] files,
    HttpServletRequest request, HttpServletResponse response
  ) throws IOException {
    if (files == null) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "There is no file in request");
      return null;
    } else {
      repositoryService.saveApp(type, fqn, files);
      return "ok";
    }
  }
}
