package com.example.web_project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SampleController {

    @GetMapping("/ex1")
    public void sample() {
        log.info("Sample server is runnning..");

    }
}
