package io.github.jzdayz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.ServerSocket;

@SpringBootApplication
@MapperScan("io.github.jzdayz.db.mapper")
public class App {

    static {
        for (int i = 10000; i < 60000; i++) {
            try {
                ServerSocket serverSocket = new ServerSocket(i);
                serverSocket.close();
                System.setProperty("server.port", String.valueOf(i));
                break;
            } catch (Exception e) {
                // ignore
            }
        }
    }

    @Configuration
    public static class WebConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**").allowedMethods("*").allowedHeaders("*").allowedOriginPatterns("*").allowCredentials(true);
        }

    }

    public static void main(String[] args) {
        System.getProperties().list(System.out);
        SpringApplication.run(App.class, args);
    }


}
