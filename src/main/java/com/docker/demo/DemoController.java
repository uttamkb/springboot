package com.docker.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @GetMapping(path="/")
    public String runImage(){
        return  "docker image created and running";

    }
    @GetMapping(path="/hello")
    public String sayHello(){
        return  "Hello - image created and running";

    }
}
