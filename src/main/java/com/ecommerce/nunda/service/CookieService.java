package com.ecommerce.nunda.service;

import jakarta.servlet.http.Cookie;

import java.util.List;

public interface CookieService {

    Cookie createCartCookie(String productIds);
    String decodeCookie(String cookieValue);


}
