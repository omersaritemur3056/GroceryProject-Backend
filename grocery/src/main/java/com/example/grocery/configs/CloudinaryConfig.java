package com.example.grocery.configs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinaryAccountSet() {

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dsfebmcdy");
        config.put("api_key", "889147229764614");
        config.put("api_secret", "EP8_6KWo4PJYnZUoPKDZvfqjf7M");
        return new Cloudinary(config);
    }

}
