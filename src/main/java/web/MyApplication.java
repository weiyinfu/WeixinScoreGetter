package web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class MyApplication extends SpringBootServletInitializer {
    static void init() {
        UserData.load();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        init();
        return builder.sources(MyApplication.class);
    }

    public static void main(String[] args) {
        init();
        SpringApplication.run(MyApplication.class, args);
    }
}