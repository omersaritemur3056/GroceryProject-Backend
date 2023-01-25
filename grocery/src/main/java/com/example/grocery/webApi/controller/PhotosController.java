package com.example.grocery.webApi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.grocery.business.abstracts.PhotoService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.webApi.responses.image.GetAllImageResponse;
import com.example.grocery.webApi.responses.image.GetByIdImageResponse;

@RestController
@RequestMapping("/api/image")
public class PhotosController {

    @Autowired
    private PhotoService photoService;

    @PostMapping("/add")
    public ResponseEntity<Result> add(MultipartFile file) {
        return ResponseEntity.ok(photoService.upload(file));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Result> delete(String imageUrl) {
        return ResponseEntity.ok(photoService.delete(imageUrl));
    }

    @GetMapping("/getall")
    public ResponseEntity<DataResult<List<GetAllImageResponse>>> getAll() {
        return new ResponseEntity<>(this.photoService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/getbyid")
    public ResponseEntity<DataResult<GetByIdImageResponse>> getById(@RequestParam Long id) {
        return new ResponseEntity<>(this.photoService.getById(id), HttpStatus.OK);
    }
}
