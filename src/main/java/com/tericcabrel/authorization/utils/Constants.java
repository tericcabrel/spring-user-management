package com.tericcabrel.authorization.utils;

public class Constants {
    public static final long TOKEN_LIFETIME_SECONDS = 24 * 60 * 60;
    public static final String SIGNING_KEY = "t3r1cc4brel2019";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTHORITIES_KEY = "scopes";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
}
