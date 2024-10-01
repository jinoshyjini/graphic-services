//package com.designer.graphic.controller;
//
//import com.designer.graphic.entities.VideoEntity;
//import com.designer.graphic.services.VideoService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.*;
//
//@RestController
//@RequestMapping("/videos")
//public class VideoController {
//    @Autowired
//    private VideoService videoService;
//
//    @PostMapping("/upload")
//    public ResponseEntity<Map<String, Object>> uploadVideo(
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("category") String category) {
//        Map<String, Object> response = new HashMap<>();
//        try {
//            long size = file.getSize();  // Log or print the file size
//            System.out.println("File size: " + size);
//
//            // Assuming the URL is retrieved or generated here (for example purposes)
//            String videoUrl = "http://example.com/path/to/video/" + file.getOriginalFilename();
//
//            // Save the video and retrieve the saved entity
//            VideoEntity savedVideo = videoService.saveVideo(file.getOriginalFilename(), file.getBytes(), videoUrl, category);
//
//            // Prepare the response with the video ID
//            response.put("status", "success");
//            response.put("message", "Video uploaded successfully");
//            response.put("id", savedVideo.getId());
//
//            return ResponseEntity.ok(response);
//        } catch (IOException e) {
//            response.put("status", "error");
//            response.put("message", "Video upload failed");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//
////    @GetMapping("/all")
////    public ResponseEntity<Map<String, Object>> getAllVideos() {
////        // Prepare response data
////        Map<String, Object> response = new HashMap<>();
////        List<VideoEntity> videoEntities = videoService.getAllVideos();
////        Map<String, List<Map<String, Object>>> videosByCategory = new HashMap<>();
////
////        // Loop through video entities and group by category
////        for (VideoEntity video : videoEntities) {
////            Map<String, Object> videoData = new HashMap<>();
////            videoData.put("id", video.getId());
////            videoData.put("filename", video.getFilename());
////            videoData.put("url", video.getUrl());
////
////            String category = video.getCategory();
////            videosByCategory
////                    .computeIfAbsent(category, k -> new ArrayList<>())
////                    .add(videoData);
////        }
////
////        // Add status and message to the response map
////        response.put("status", "success");
////        response.put("message", "Videos retrieved successfully");
////        response.put("videosByCategory", videosByCategory); // Add the grouped videos
////
////        // Return the response
////        return ResponseEntity.ok(response);
////    }
//
//    @GetMapping("/all")
//    public ResponseEntity<Map<String, Object>> getAllVideos(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size) {
//
//        // Fetch paginated results
//        Page<VideoEntity> paginatedVideos = videoService.getPaginatedVideos(page, size);
//        Map<String, Object> response = new HashMap<>();
//
//        // Prepare response data
//        List<VideoEntity> videoEntities = paginatedVideos.getContent();
//        Map<String, List<Map<String, Object>>> videosByCategory = new HashMap<>();
//
//        // Loop through video entities and group by category
//        for (VideoEntity video : videoEntities) {
//            Map<String, Object> videoData = new HashMap<>();
//            videoData.put("id", video.getId());
//            videoData.put("filename", video.getFilename());
//            videoData.put("url", video.getUrl());
//
//            String category = video.getCategory();
//            videosByCategory
//                    .computeIfAbsent(category, k -> new ArrayList<>())
//                    .add(videoData);
//        }
//
//        // Add pagination details and response data to the map
//        response.put("status", "success");
//        response.put("message", "Videos retrieved successfully");
//        response.put("videosByCategory", videosByCategory); // Add the grouped videos
//        response.put("totalPages", paginatedVideos.getTotalPages());
//        response.put("totalElements", paginatedVideos.getTotalElements());
//        response.put("currentPage", paginatedVideos.getNumber());
//        response.put("pageSize", paginatedVideos.getSize());
//
//        // Return the response
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/video/{id}")
//    public ResponseEntity<byte[]> getVideo(@PathVariable Integer id) {
//        VideoEntity video = videoService.getVideo(id);
//        if (video != null) {
//            return ResponseEntity.ok()
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM) // Adjust content type as needed
//                    .body(video.getContent());
//        }
//        return ResponseEntity.notFound().build();
//    }
//}


package com.designer.graphic.controller;

import com.designer.graphic.entities.VideoEntity;
import com.designer.graphic.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/videos")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("category") String category) {
        Map<String, Object> response = new HashMap<>();
        try {
            VideoEntity savedVideo = videoService.saveVideo(file, category);
            response.put("status", "success");
            response.put("message", "Video uploaded successfully");
            response.put("id", savedVideo.getId());
            response.put("url", savedVideo.getUrl());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Video upload failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllVideos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<VideoEntity> paginatedVideos = videoService.getPaginatedVideos(page, size);
        Map<String, Object> response = new HashMap<>();

        Map<String, List<Map<String, Object>>> videosByCategory = paginatedVideos.getContent().stream()
                .collect(Collectors.groupingBy(
                        VideoEntity::getCategory,
                        Collectors.mapping(
                                video -> {
                                    Map<String, Object> videoData = new HashMap<>();
                                    videoData.put("id", video.getId());
                                    videoData.put("filename", video.getFilename());
                                    videoData.put("url", video.getUrl());
                                    return videoData;
                                },
                                Collectors.toList()
                        )
                ));

        response.put("status", "success");
        response.put("message", "Videos retrieved successfully");
        response.put("videosByCategory", videosByCategory);
        response.put("totalPages", paginatedVideos.getTotalPages());
        response.put("totalElements", paginatedVideos.getTotalElements());
        response.put("currentPage", paginatedVideos.getNumber());
        response.put("pageSize", paginatedVideos.getSize());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/video/{filename}")
    public ResponseEntity<Resource> serveVideo(@PathVariable String filename) {
        try {
            Path videoPath = videoService.getVideoPath(filename);
            Resource resource = new UrlResource(videoPath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType("video/mp4"))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getVideosByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<VideoEntity> paginatedVideos = videoService.getPaginatedVideosByCategory(category, page, size);

        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> videoList = paginatedVideos.getContent().stream()
                .map(video -> {
                    Map<String, Object> videoData = new HashMap<>();
                    videoData.put("id", video.getId());
                    videoData.put("filename", video.getFilename());
                    videoData.put("url", video.getUrl());
                    return videoData;
                })
                .collect(Collectors.toList());

        response.put("status", "success");
        response.put("message", "Videos retrieved successfully");
        response.put("category", category);
        response.put("videos", videoList);
        response.put("totalPages", paginatedVideos.getTotalPages());
        response.put("totalElements", paginatedVideos.getTotalElements());
        response.put("currentPage", paginatedVideos.getNumber());
        response.put("pageSize", paginatedVideos.getSize());

        return ResponseEntity.ok(response);
    }

}