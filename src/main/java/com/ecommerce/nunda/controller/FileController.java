package com.ecommerce.nunda.controller;

import com.ecommerce.nunda.configs.FileHandlerConfig;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class FileController {
    private final FileHandlerConfig fileHandlerConfig;


    public FileController(FileHandlerConfig fileHandlerConfig) {
        this.fileHandlerConfig = fileHandlerConfig;
    }

    @GetMapping("/images/products/{filename}")
    public ResponseEntity<Resource> getProductImage(@PathVariable String filename) {
        String productLocation = fileHandlerConfig.getProductLocation();
        try {
            Path filePath = Paths.get(productLocation).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                // Serve fallback image
                Path fallbackPath = Paths.get(productLocation).resolve("default-image.jpg").normalize();
                Resource fallbackResource = new UrlResource(fallbackPath.toUri());
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fallbackResource.getFilename() + "\"")
                        .body(fallbackResource);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}