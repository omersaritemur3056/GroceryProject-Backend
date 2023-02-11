package com.example.grocery.layeredTest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.grocery.webApi.controller.PhotosController;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PhotosContorllerTest {

    @Autowired
    private PhotosController photosController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(photosController).isNotNull();

    }
}
