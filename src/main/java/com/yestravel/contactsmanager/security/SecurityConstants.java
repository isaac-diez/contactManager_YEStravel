package com.yestravel.contactsmanager.security;

public class SecurityConstants {
    public static final String SECRET_KEY = "YOUR_SECRET_KEY_CHANGE_THIS";
    public static final long EXPIRATION_TIME = 86400000; // 24h
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
