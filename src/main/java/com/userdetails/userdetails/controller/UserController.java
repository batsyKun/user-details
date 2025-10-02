package com.userdetails.userdetails.controller;

import com.userdetails.userdetails.entity.User;
import com.userdetails.userdetails.service.UserService;
import com.userdetails.userdetails.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<User> createUser(
            @RequestPart("user") @Valid User user,
            @RequestPart(value = "documents", required = false) List<MultipartFile> documents,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {

        if (documents != null && !documents.isEmpty()) {
            List<String> docPaths = fileStorageService.storeMultipleFiles(documents, "documents");
            user.setDocumentPaths(docPaths);
        }
        if (photo != null && !photo.isEmpty()) {
            String photoPath = fileStorageService.storeFile(photo, "photos");
            user.setPhotoPath(photoPath);
        }
        User created = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestPart("user") @Valid User user,
            @RequestPart(value = "documents", required = false) List<MultipartFile> documents,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {

        if (documents != null && !documents.isEmpty()) {
            List<String> docPaths = fileStorageService.storeMultipleFiles(documents, "documents");
            user.setDocumentPaths(docPaths);
        }
        if (photo != null && !photo.isEmpty()) {
            String photoPath = fileStorageService.storeFile(photo, "photos");
            user.setPhotoPath(photoPath);
        }
        User updated = userService.updateUser(id, user);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}