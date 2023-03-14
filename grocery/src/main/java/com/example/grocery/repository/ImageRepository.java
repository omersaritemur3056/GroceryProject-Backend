package com.example.grocery.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.model.concretes.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByUrl(String imageUrl);
}
