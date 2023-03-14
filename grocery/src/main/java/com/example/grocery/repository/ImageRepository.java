package com.example.grocery.dataAccess.abstracts;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.entity.concretes.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByUrl(String imageUrl);
}
