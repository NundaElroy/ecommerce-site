package com.ecommerce.nunda.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "filehandler")
public class FileHandlerConfig {
    private String productLocation;
    private String profileLocation;
    private String excelFileLocation;

    public String getExcelFileLocation() {
        return excelFileLocation;
    }

    public void setExcelFileLocation(String excelFileLocation) {
        this.excelFileLocation = excelFileLocation;
    }




    public String getProfileLocation() {
        return profileLocation;
    }

    public void setProfileLocation(String profileLocation) {
        this.profileLocation = profileLocation;
    }

    public String getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(String productLocation) {
        this.productLocation = productLocation;
    }



}
