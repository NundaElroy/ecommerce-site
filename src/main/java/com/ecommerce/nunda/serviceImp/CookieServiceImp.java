package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.service.CookieService;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;

import java.util.List;

@Service
public class CookieServiceImp implements CookieService {


    private final String domain;

    public CookieServiceImp(@Value("${cookie.domain}") String domain) {
        this.domain = domain;
    }

    @Override
    public Cookie createCartCookie(String productIds) {
        Cookie cookie = new Cookie("usercart", encodeCookie(productIds));
        cookie.setDomain(domain);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7 * 24 * 60 * 60);//expires after 7 days
        cookie.setPath("/");//will be sent to any path

        return cookie;
    }


    private String encodeCookie(String jsonFormat) {
        return URLEncoder.encode(jsonFormat, StandardCharsets.UTF_8);
    }

    public  String decodeCookie(String cookieValue) {
        return URLDecoder.decode(cookieValue, StandardCharsets.UTF_8);
    }
}
