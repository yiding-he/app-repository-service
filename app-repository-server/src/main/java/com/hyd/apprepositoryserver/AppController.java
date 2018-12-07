package com.hyd.apprepositoryserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class AppController {

    @Value("${repository-root}")
    private String repositoryRoot;

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello!";
    }

    @RequestMapping("/apps/{appName}/class-path")
    @ResponseBody
    public String getClassPath(@PathVariable("appName") String appName) {
        File libRoot = new File(repositoryRoot, "apps/" + appName + "/lib");

        File[] jars = libRoot.listFiles(
                f -> f.isFile() && f.getName().endsWith(".jar"));

        if (jars != null) {
            return Stream.of(jars).map(File::getName).collect(Collectors.joining(";"));
        }

        return "";
    }
}
