package fr.bibliotheque;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BibliothequeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliothequeApplication.class);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowCredentials(true)
                        .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE")
                        .exposedHeaders("Access-Control-Allow-Origin")
                        .allowedOrigins(
                                "http://localhost",
                                "http://158.69.21.143",
                                "http://fil-rouge-2.xyz",
                                "https://fil-rouge-2.xyz");
            }
        };
    }
}
