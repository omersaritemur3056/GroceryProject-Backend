package com.example.grocery.layeredTest.service;

import com.example.grocery.business.concretes.PhotoManager;
import com.example.grocery.core.utilities.image.ImageService;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.dataAccess.abstracts.ImageRepository;
import com.example.grocery.entity.concretes.Image;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PhotoManagerTest {

    @Mock
    private ImageService imageService;

    @Mock
    private MapperService mapperService;

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private PhotoManager photoManager;

    private Image image, image2;

    @BeforeEach
    public void setUp() {
        image = Image.builder()
                .id(1L).publicId("brpvbafwcaensd5avbrz")
                .url("http://res.cloudinary.com/dsfebmcdy/image/upload/v1675564068/brpvbafwcaensd5avbrz.png")
                .bytes(15814).format("png").height(1034).width(920).build();
        image2 = Image.builder()
                .id(2L).publicId("b9qkeq4ouzbgopkadbsh")
                .url("http://res.cloudinary.com/dsfebmcdy/image/upload/v1675565268/b9qkeq4ouzbgopkadbsh.png")
                .bytes(7091).format("png").height(920).width(920).build();
    }

    @Test
    public void givenImageObjectShouldSave() throws Exception {
        Image savedImage = imageRepository.save(image);
        Image savedImage2 = imageRepository.save(image2);
    }

    @Test
    public void givenId_whenFoundImageShouldUpdate() throws Exception {
        Image image3 = Image.builder().id(3L).publicId("mfbf8havnf89cpwkuttg").bytes(7091).format("png").height(920)
                .url("http://res.cloudinary.com/dsfebmcdy/image/upload/v1675576693/mfbf8havnf89cpwkuttg.png")
                .width(920).build();
        assertThat(imageRepository.findByUrl(image3.getUrl())).isNotNull();

        image3.setBytes(12311);
        image3.setHeight(1011);
        image3.setWidth(1080);
        image3.setFormat("jpg");
        imageRepository.save(image3);

        assertEquals("Success -> ", image3.getFormat(), "jpg");
    }

    @Test
    public void givenUrlShouldBeDeleteImageObject() throws Exception {
        Image image3 = Image.builder().id(3L).publicId("mfbf8havnf89cpwkuttg").bytes(7091).format("png").height(920)
                .url("http://res.cloudinary.com/dsfebmcdy/image/upload/v1675576693/mfbf8havnf89cpwkuttg.png")
                .width(920).build();
        imageRepository.delete(image3);
        verify(imageRepository, times(1)).delete(image3);
    }

    @Test
    public void imageListShouldReturn() throws Exception {
        assertThat(photoManager.getAll()).isNotNull();
    }

    @Test
    public void anyServiceMissOut() {
        assertThat(mapperService.getModelMapper()).isNull();
    }
}
