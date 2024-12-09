package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.configs.FileHandlerConfig;
import com.ecommerce.nunda.customexceptions.EmptyFileException;
import com.ecommerce.nunda.customexceptions.FileLocationNotFoundException;
import com.ecommerce.nunda.service.FileStorageHandlerService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageHandlerServiceImp implements FileStorageHandlerService {

    private  final FileHandlerConfig fileHandlerConfig;

    public FileStorageHandlerServiceImp(FileHandlerConfig fileHandlerConfig) {
        this.fileHandlerConfig = fileHandlerConfig;
    }

    @Override
    public String storeProductImages(MultipartFile file) {

        if(file.isEmpty()){
            throw new EmptyFileException("Failed to store empty file.");
        }

        //get filename
        String originalFilename = file.getOriginalFilename();

        //file location
        String fileLocation = fileHandlerConfig.getProductLocation();

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


        return uniqueFilename;
    }
}
