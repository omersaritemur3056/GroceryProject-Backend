package com.example.grocery.api.responses.image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetByUrlImageResponse {

    private Long id;
    private String publicId;
    private String url;
    private int bytes;
    private String format;
    private int height;
    private int width;
}
