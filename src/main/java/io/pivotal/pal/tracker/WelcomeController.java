package io.pivotal.pal.tracker;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
    String mess;

    public WelcomeController(@Value("${welcome.message}") String message) {
        this.mess= message;
    }

    @GetMapping("/")
    public String sayHello() {
        return this.mess;
    }
}