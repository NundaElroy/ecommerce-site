package com.ecommerce.nunda.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageHandlerService {
    String storeProductImages(MultipartFile file);
}
