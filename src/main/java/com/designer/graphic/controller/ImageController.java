package com.designer.graphic.controller;

import com.designer.graphic.entities.ImageEntity;
import com.designer.graphic.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("category") String category) {
        Map<String, Object> response = new HashMap<>();
        try {
            long size = file.getSize();  // Log or print the file size
            System.out.println("File size: " + size);

            // Assuming the URL is retrieved or generated here (for example purposes)
            String imageUrl = "http://example.com/path/to/image/" + file.getOriginalFilename();

            // Save the image and retrieve the saved entity
            ImageEntity savedImage = imageService.saveImage(file.getOriginalFilename(), file.getBytes(), imageUrl, category);

            // Prepare the response with the image ID
            response.put("status", "success");
            response.put("message", "Image uploaded successfully");
            response.put("id", savedImage.getId());

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Image upload failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllImages() {
        // Prepare response data
        Map<String, Object> response = new HashMap<>();
        List<ImageEntity> imageEntities = imageService.getAllImages();
        List<Map<String, Object>> images = new ArrayList<>();

        // Loop through image entities and prepare image data with Base64 content
        for (ImageEntity image : imageEntities) {
            Map<String, Object> imageData = new HashMap<>();
            imageData.put("id", image.getId());
            imageData.put("filename", image.getFilename());
            imageData.put("category", image.getCategory()); // Add category
            String base64Content = Base64.getEncoder().encodeToString(image.getContent());
            imageData.put("content", "data:image/png;base64," + base64Content);

            images.add(imageData);
        }

        // Add status and message to the response map
        response.put("status", "success");
        response.put("message", "Images retrieved successfully");
        response.put("images", images); // Add the list of images

        // Return the response
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getImagesByCategory(@PathVariable String category) {
        // Prepare response data
        Map<String, Object> response = new HashMap<>();
        List<ImageEntity> imageEntities = imageService.getImagesByCategory(category);
        List<Map<String, Object>> images = new ArrayList<>();

        // Loop through image entities and prepare image data with Base64 content
        for (ImageEntity image : imageEntities) {
            Map<String, Object> imageData = new HashMap<>();
            imageData.put("id", image.getId());
            imageData.put("filename", image.getFilename());
            String base64Content = Base64.getEncoder().encodeToString(image.getContent());
            imageData.put("content", "data:image/png;base64," + base64Content);

            images.add(imageData);
        }

        // Add status and message to the response map
        response.put("status", "success");
        response.put("message", "Images retrieved successfully by category");
        response.put("images", images); // Add the list of images

        // Return the response
        return ResponseEntity.ok(response);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) {
        ImageEntity image = imageService.getImage(id);
        if (image != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG) // Adjust content type as needed
                    .body(image.getContent());
        }
        return ResponseEntity.notFound().build();
    }
}

