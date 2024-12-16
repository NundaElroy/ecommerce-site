package com.ecommerce.nunda.service;


import org.springframework.web.multipart.MultipartFile;

public interface ExcelFileService {
    String saveFile(MultipartFile file);
}
