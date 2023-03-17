package com.example.grocery.apiIntegrationTest;

import com.example.grocery.api.responses.image.GetAllImageResponse;
import com.example.grocery.api.responses.image.GetByIdImageResponse;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.model.concretes.Image;
import com.example.grocery.repository.ImageRepository;
import com.example.grocery.service.interfaces.PhotoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Type;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PhotoApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PhotoService photoService;
    @Autowired
    private ImageRepository imageRepository;

    private static HttpHeaders headers;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void init(){
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @Sql(statements = "INSERT INTO images(image_id, image_public_id, image_url, image_bytes, image_format, " +
            "image_height, image_width) VALUES (40, 'brpvbafwcaensd5avbrz', " +
            "'http://res.cloudinary.com/dsfebmcdy/image/upload/v1675564068/brpvbafwcaensd5avbrz.png', " +
            "15814, 'png', 1034,920)",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM images WHERE image_id='40'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetPhotos(){
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/image/getall"),
                HttpMethod.GET, entity, String.class);
        var imageList = response.getBody();
        assert imageList != null;
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        System.out.println(imageList);
        assertEquals(imageList.contains(photoService.getAll().getData().get(0).getPublicId().toString()), true);
        assertEquals(imageList.contains(imageRepository.findAll().stream().filter(i ->
                i.getId() == 40).findFirst().orElse(null).getUrl()), true);
    }

    @Test
    @Sql(statements = "INSERT INTO images(image_id, image_public_id, image_url, image_bytes, image_format, " +
            "image_height, image_width) VALUES (41, 'b9qkeq4ouzbgopkadbsh', " +
            "'http://res.cloudinary.com/dsfebmcdy/image/upload/v1675565268/b9qkeq4ouzbgopkadbsh.png', " +
            "7091, 'png', 920,920)",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM images WHERE image_id='41'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetPhotosById(){
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/image/getbyid/41"), HttpMethod.GET, entity, String.class);
        var responseBody = response.getBody();
        assert responseBody != null;
        System.out.println(responseBody);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(responseBody.contains(photoService.getById(41L).getData().getUrl()), true);
        assertEquals(responseBody.contains(imageRepository.findById(41L).get().getUrl()), true);
    }

    /*@Test
    @Sql(statements = "DELETE FROM images WHERE image_id='42'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testCreateImage() throws JsonProcessingException {
        MockMultipartFile file = new MockMultipartFile("HTML - Kopya.png",
                "classpath:HTML - Kopya.png".getBytes());
        HttpEntity<String> entity = new HttpEntity<>(file.getContentType(), headers);
        ResponseEntity<Image> response = restTemplate.exchange(createURLWithPort("/api/image/add"),
                HttpMethod.POST, entity, Image.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }*/

    @Test
    @Sql(statements = "INSERT INTO images(image_id, image_public_id, image_url, image_bytes, image_format, " +
            "image_height, image_width) VALUES (46, 'brpvbafwcaensd5avbrz', " +
            "'http://res.cloudinary.com/dsfebmcdy/image/upload/v1675564068/brpvbafwcaensd5avbrz.png', " +
            "15814, 'png', 1034,920)",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM images WHERE image_id='46'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testDeleteImageFromDb() throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/image/deletefromdbbyid/46"),
                HttpMethod.DELETE, null, String.class);
        String deleteResponse = response.getBody();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(deleteResponse);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
