package com.tericcabrel.authorization.utils;

public class Constants {
    static final long TOKEN_LIFETIME_SECONDS = 24 * 60 * 60;
    static final String SIGNING_KEY = "t3r1cc4brel2019";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    static final String AUTHORITIES_KEY = "scopes";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final String JWT_ILLEGAL_ARGUMENT_MESSAGE = "An error occured during getting username from token";
    public static final String JWT_EXPIRED_MESSAGE = "The token is expired and not valid anymore";
    public static final String JWT_SIGNATURE_MESSAGE = "Authentication Failed. Username or Password not valid.";
}
