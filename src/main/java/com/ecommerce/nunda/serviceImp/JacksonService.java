package com.ecommerce.nunda.serviceImp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JacksonService {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    //convert List of productsId to string
    public  String convertProductIdsToString(List<String> productIds) throws JsonProcessingException {
        return  objectMapper.writeValueAsString(productIds);
    }

    //convert back to List of products
    public List<String> convertStringCookieToList(String usercart) throws JsonProcessingException {
        return  objectMapper.readValue(usercart, new TypeReference<List<String>>() {});
    }

}
