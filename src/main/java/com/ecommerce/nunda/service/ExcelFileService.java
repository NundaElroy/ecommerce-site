package com.ecommerce.nunda.service;


import org.apache.poi.ss.usermodel.Cell;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelFileService {
    String saveFile(MultipartFile file);
    String getCellValue(Cell cell);
}
