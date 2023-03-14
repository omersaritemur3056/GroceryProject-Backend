package com.example.grocery.webApi.responses.image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllImageResponse {

    private Long id;
    private String publicId;
    private String url;
    private int bytes;
    private String format;
    private int height;
    private int width;
}
