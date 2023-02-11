package com.example.grocery.layeredTest.controller;

import com.example.grocery.business.abstracts.PhotoService;
import com.example.grocery.dataAccess.abstracts.ImageRepository;
import com.example.grocery.entity.concretes.Image;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.grocery.webApi.controller.PhotosController;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

@WebMvcTest(PhotosController.class)
public class PhotosContorllerTest {

        @Autowired
        private MockMvc mockMvc;
        @MockBean
        private PhotoService photoService;
        @MockBean
        private ImageRepository imageRepository;

        @Test
        @WithMockUser(roles = "ADMIN")
        public void shouldReturnListOfImages() throws Exception {
                Image image = Image.builder()
                                .id(1L).publicId("brpvbafwcaensd5avbrz")
                                .url("http://res.cloudinary.com/dsfebmcdy/image/upload/v1675564068/brpvbafwcaensd5avbrz.png")
                                .bytes(15814).format("png").height(1034).width(920).build();
                when(imageRepository.findAll()).thenReturn(Arrays.asList(image));

                mockMvc.perform(get("/api/image/getall")).andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        public void shouldReturnImageWhenFound() throws Exception {
                Image image = Image.builder()
                                .id(2L).publicId("b9qkeq4ouzbgopkadbsh")
                                .url("http://res.cloudinary.com/dsfebmcdy/image/upload/v1675565268/b9qkeq4ouzbgopkadbsh.png")
                                .bytes(7091).format("png").height(920).width(920).build();
                when(imageRepository.findById(2L)).thenReturn(Optional.of(image));

                mockMvc.perform(get("/api/image/getbyid/2")).andExpect(status().isOk())
                                .andReturn().getResponse();
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        public void shouldAddImage() throws Exception {
                Image image = Image.builder()
                                .id(2L).publicId("b9qkeq4ouzbgopkadbsh")
                                .url("http://res.cloudinary.com/dsfebmcdy/image/upload/v1675565268/b9qkeq4ouzbgopkadbsh.png")
                                .bytes(7091).format("png").height(920).width(920).build();
                when(imageRepository.save(any(Image.class))).thenReturn(image);

                mockMvc.perform(post("/api/image/add").contentType(MediaType.APPLICATION_JSON)
                                .content(image.toString()))
                                .andExpect(status().isOk());
        }

        @Test
        public void shouldDeleteImage() throws Exception {
                Image image = Image.builder()
                                .id(2L).publicId("b9qkeq4ouzbgopkadbsh")
                                .url("http://res.cloudinary.com/dsfebmcdy/image/upload/v1675565268/b9qkeq4ouzbgopkadbsh.png")
                                .bytes(7091).format("png").height(920).width(920).build();
                when(photoService.delete(image.getUrl())).thenReturn(null);

        }

        @Test
        @WithMockUser(roles = "ADMIN")
        public void shouldUpdateImage() throws Exception {
                Image image = Image.builder()
                                .id(1L).publicId("brpvbafwcaensd5avbrz")
                                .url("http://res.cloudinary.com/dsfebmcdy/image/upload/v1675564068/brpvbafwcaensd5avbrz.png")
                                .bytes(15814).format("png").height(1034).width(920).build();
                when(imageRepository.save(image)).thenReturn(any(Image.class));

                mockMvc.perform(put("/api/image/update").contentType(MediaType.APPLICATION_JSON)
                                .content(anyString())).andExpect(status().isOk());
        }

}
