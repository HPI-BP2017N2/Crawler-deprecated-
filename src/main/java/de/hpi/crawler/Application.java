package de.hpi.crawler;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication (scanBasePackages = {"de.hpi"})
public class Application {

    public static void main(String... args) {
        new SpringApplicationBuilder(Application.class).run(args);

    }
}


