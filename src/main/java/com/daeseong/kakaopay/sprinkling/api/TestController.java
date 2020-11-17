package com.daeseong.kakaopay.sprinkling.api;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"0. test"})
@RestController
@RequestMapping("/v1")
public class TestController {

    @GetMapping("/test")
    public String getHelloWorld() {
        return "hello world!";
    }
}
