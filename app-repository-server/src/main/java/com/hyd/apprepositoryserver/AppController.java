package com.hyd.apprepositoryserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AppController {

    @Autowired
    private RepositoryConfig repositoryConfig;

    @RequestMapping({"", "/"})
    @ResponseBody
    public String index() {

        List<String> apps = listApps().stream()
                .map(s -> "  - " + s)
                .collect(Collectors.toList());

        return "================================================\n" +
                "欢迎使用 Application Repository Server！\n" +
                "执行下面的命令来启动 App：\n\n" +
                "  curl -s " + repositoryConfig.getUrlPrefix() + "/apps/APP_NAME/start | sh\n\n" +
                "可用的 APP_NAME: \n" + String.join("\n", apps) + "\n" +
                "================================================\n";
    }

    private List<String> listApps() {
        try {
            return Files.list(Paths.get(repositoryConfig.getRoot(), "apps"))
                    .filter(Files::isDirectory)
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
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
            @RequestParam(name = "args", required = false, defaultValue = "") String args
    ) throws Exception {
        return readResource(appName, "classpath:templates/start.template.sh", args);
    }

    @RequestMapping("/apps/{appName}/start-boot")
    @ResponseBody
    public String startSpringBootScript(
            @PathVariable("appName") String appName,
            @RequestParam(name = "args", required = false, defaultValue = "") String args
    ) throws Exception {
        return readResource(appName, "classpath:templates/start.template.sb.sh", args);
    }

    private String readResource(String appName, String path, String args) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        String script = FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream()));
        String urlPrefix = repositoryConfig.getUrlPrefix();

        return script
                .replace("{{args}}", args)
                .replace("{{urlPrefix}}", urlPrefix)
                .replace("{{appname}}", appName);
    }
}
