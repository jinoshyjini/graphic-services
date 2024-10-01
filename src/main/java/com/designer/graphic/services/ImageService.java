package com.designer.graphic.services;

import com.designer.graphic.entities.ImageEntity;
import com.designer.graphic.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public ImageEntity saveImage(String filename, byte[] content, String url, String category) {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setFilename(filename);
        imageEntity.setContent(content);
        imageEntity.setUrl(url);  // Save the URL
        imageEntity.setCategory(category); // Save the category
        return imageRepository.save(imageEntity);
    }

    public List<ImageEntity> getAllImages() {
        return imageRepository.findAll();
    }

    public List<ImageEntity> getImagesByCategory(String category) {
        return imageRepository.findByCategory(category);
    }

    public ImageEntity getImage(Integer id) {
        return imageRepository.findById(id).orElse(null);
    }
}

