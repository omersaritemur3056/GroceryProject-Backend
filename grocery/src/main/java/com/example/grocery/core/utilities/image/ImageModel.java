package com.example.grocery.core.utilities.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class ImageModel {

    private Long id;
    private String publicId;
    private String url;
    private int bytes;
    private String format;
    private int height;
    private int width;
}
