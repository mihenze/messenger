package com.yakimenko.messenger.security.config;

public class SecurityConstants {
    //url для авторизации
    public static final String SIGN_UP_URLS = "/auth/*";

    //для генерации токена
    public static final String SECRET = "SecretKeyGenJWT";
    public static final String TOKEN_PREFIX = "Bearer_";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
}
