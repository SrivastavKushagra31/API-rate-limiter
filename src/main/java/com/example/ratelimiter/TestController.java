package com.example.ratelimiter;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class TestController {

    private final RateLimitFilter rateLimitFilter;

    public TestController(RateLimitFilter rateLimitFilter) {
        this.rateLimitFilter = rateLimitFilter;
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    @GetMapping("/reset")
    public String reset() {
        rateLimitFilter.reset();
        return "OK";
    }
}