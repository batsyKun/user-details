package com.userdetails.userdetails.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageService {

    @Value("${file.storage.location}")
    private String fileStorageLocation;

    public String storeFile(MultipartFile file, String subDir) {
        try {
            Path dirPath = Paths.get(fileStorageLocation, subDir);
            Files.createDirectories(dirPath);

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = dirPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filePath.toString();
        } catch (Exception e) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename(), e);
        }
    }

    public List<String> storeMultipleFiles(List<MultipartFile> files, String subDir) {
        List<String> paths = new ArrayList<>();
        for (MultipartFile file : files) {
            paths.add(storeFile(file, subDir));
        }
        return paths;
    }
}
