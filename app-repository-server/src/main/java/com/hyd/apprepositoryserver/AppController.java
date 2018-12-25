package com.hyd.apprepositoryserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class AppController {

    @Autowired
    private RepositoryConfig repositoryConfig;

    @RequestMapping({"", "/"})
    @ResponseBody
    public String index() {
        return "欢迎使用 Application Repository Server！\n" +
                "请执行下面的命令之一：\n" +
                "  wget " + repositoryConfig.getUrlPrefix() + "/apps/APP_NAME/start -O - | sh\n" +
                "  curl -s "+repositoryConfig.getUrlPrefix() +"/apps/APP_NAME/start | sh";
    }

    @RequestMapping("/apps/{appName}/class-path")
    @ResponseBody
    public String getClassPath(@PathVariable("appName") String appName) {
        File libRoot = new File(repositoryConfig.getRoot(), "apps/" + appName + "/lib");

        File[] jars = libRoot.listFiles(
                f -> f.isFile() && f.getName().endsWith(".jar"));

        if (jars != null) {
            return Stream.of(jars).map(File::getName).collect(Collectors.joining(";"));
        }

        return "";
    }

    @RequestMapping("/apps/{appName}/start")
    @ResponseBody
    public String startScript(
            @PathVariable("appName") String appName,
            @RequestParam(required = false, defaultValue = "") String args
    ) throws Exception {

        URI uri = AppController.class.getResource("/templates/start.template.sh").toURI();
        Path path = Paths.get(uri);
        String script = new String(Files.readAllBytes(path));
        String urlPrefix = repositoryConfig.getUrlPrefix();

        return script
                .replace("{{args}}", args)
                .replace("{{urlPrefix}}", urlPrefix)
                .replace("{{appname}}", appName);
    }
}
