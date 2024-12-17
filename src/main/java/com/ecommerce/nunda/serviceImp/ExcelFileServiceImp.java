package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.configs.FileHandlerConfig;
import com.ecommerce.nunda.customexceptions.EmptyFileException;
import com.ecommerce.nunda.customexceptions.FileLocationNotFoundException;
import com.ecommerce.nunda.service.ExcelFileService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ExcelFileServiceImp implements ExcelFileService {
    private final FileHandlerConfig fileHandlerConfig;
    private static final Logger logger = LoggerFactory.getLogger(ExcelFileServiceImp.class);
    @Value("${file.upload-dir}")
    private String uploadDir;


    public ExcelFileServiceImp(FileHandlerConfig fileHandlerConfig) {
        this.fileHandlerConfig = fileHandlerConfig;
    }

    //save excel file method
    @Override
    public String saveFile(MultipartFile file) {
        if(file.isEmpty()){
            throw new EmptyFileException("Failed to store empty file.");
        }

        //get filename
        String originalFilename = file.getOriginalFilename();

        //file location
        String fileLocation = fileHandlerConfig.getExcelFileLocation();

        if(fileLocation == null){
            throw  new FileLocationNotFoundException("file location not found");
        }


        // Generate a unique filename
        String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;

        try{

            Path filelocationToPath = Paths.get(fileLocation);


            if (!Files.exists(filelocationToPath)) {
                Files.createDirectories(filelocationToPath);
            }

            Path finalDestination = filelocationToPath.resolve(uniqueFilename);

            file.transferTo(finalDestination.toFile());

        }catch (IOException e){

            throw new FileLocationNotFoundException("storage failed ",e);

        }
        //get relative location of excel file
        String resolvedPath = Paths.get(uploadDir).resolve(uniqueFilename).normalize().toString();
        logger.info("File saved successfully at: {}", resolvedPath);

        return resolvedPath;
    }

    // Helper method to get the cell value
    public String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return String.valueOf(cell.getDateCellValue());
                }
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }
}

