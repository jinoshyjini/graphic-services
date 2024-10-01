//package com.designer.graphic.services;
//
//import com.designer.graphic.entities.VideoEntity;
//import com.designer.graphic.repository.VideoRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class VideoService {
//    @Autowired
//    private VideoRepository videoRepository;
//
//    public VideoEntity saveVideo(String filename, byte[] content, String url, String category) {
//        VideoEntity videoEntity = new VideoEntity();
//        videoEntity.setFilename(filename);
//        videoEntity.setContent(content);
//        videoEntity.setUrl(url);
//        videoEntity.setCategory(category);
//        System.out.println("Content size: " + videoEntity.getContent().length + " bytes");
//        return videoRepository.save(videoEntity);
//    }
//
//    public List<VideoEntity> getAllVideos() {
//        return videoRepository.findAll();
//    }
//
//    public Page<VideoEntity> getPaginatedVideos(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return videoRepository.findAll(pageable);
//    }
//    public VideoEntity getVideo(Integer id) {
//        return videoRepository.findById(id).orElse(null);
//    }
//}



package com.designer.graphic.services;

import com.designer.graphic.entities.VideoEntity;
import com.designer.graphic.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;

    @Value("${video.upload.dir}")
    private String uploadDir;

    @Value("${video.base.url}")
    private String baseUrl;

    public VideoEntity saveVideo(MultipartFile file, String category) throws IOException {
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate a unique filename
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Save file to the server
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath);

        // Generate URL
        String url = baseUrl + "/" + filename;

        // Save video metadata to database
        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setFilename(filename);
        videoEntity.setUrl(url);
        videoEntity.setCategory(category);

        return videoRepository.save(videoEntity);
    }

    public Page<VideoEntity> getPaginatedVideos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return videoRepository.findAll(pageable);
    }

    public Page<VideoEntity> getPaginatedVideosByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return videoRepository.findByCategory(category, pageable);
    }

//    public VideoEntity getVideo(Integer id) {
//        return videoRepository.findById(id).orElse(null);
//    }

    public VideoEntity getVideo(Long id) {
        return videoRepository.findById(id).orElse(null);
    }

    public Path getVideoPath(String filename) {
        return Paths.get(uploadDir).resolve(filename);
    }
}
