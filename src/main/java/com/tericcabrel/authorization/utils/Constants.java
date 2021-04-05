package com.tericcabrel.authorization.utils;

public class Constants {
    static final long TOKEN_LIFETIME_SECONDS = 24 * 60 * 60;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    static final String AUTHORITIES_KEY = "scopes";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";

    public static final String JWT_ILLEGAL_ARGUMENT_MESSAGE = "An error occured during getting username from token";
    public static final String JWT_EXPIRED_MESSAGE = "The token is expired and not valid anymore";
    public static final String JWT_SIGNATURE_MESSAGE = "Authentication Failed. Username or Password not valid.";
    public static final String UNAUTHORIZED_MESSAGE = "You are not authorized to view the resource";
    public static final String FORBIDDEN_MESSAGE = "You don't have the right to access to this resource";
    public static final String INVALID_DATA_MESSAGE = "One or many parameters in the request's body are invalid";

    public static final String MESSAGE_KEY = "message";
    public static final String DATA_KEY = "message";
    public static final String INVALID_TOKEN_MESSAGE = "The token is invalid!";
    public static final String TOKEN_EXPIRED_MESSAGE = "You token has been expired!";
    public static final String ACCOUNT_DEACTIVATED_MESSAGE = "Your account has been deactivated!";
    public static final String ACCOUNT_NOT_CONFIRMED_MESSAGE = "Your account isn't confirmed yet!";
    public static final String ACCOUNT_CONFIRMED_MESSAGE = "Your account confirmed successfully!";
    public static final String NO_USER_FOUND_WITH_EMAIL_MESSAGE = "No user found with this email!";
    public static final String PASSWORD_LINK_SENT_MESSAGE = "A password reset link has been sent to your email box!";
    public static final String RESET_PASSWORD_SUCCESS_MESSAGE = "Your password has been resetted successfully!";
    public static final String VALIDATE_TOKEN_SUCCESS_MESSAGE = "valid";
    public static final String TOKEN_NOT_FOUND_MESSAGE = "You token has been expired!";
    public static final String PASSWORD_NOT_MATCH_MESSAGE = "The current password don't match!";
    public static final String USER_PICTURE_NO_ACTION_MESSAGE = "Unknown action!";

    public static final String SWG_AUTH_TAG_NAME = "Registration & Authentication";
    public static final String SWG_AUTH_TAG_DESCRIPTION = "Operations pertaining to registration, authentication and account confirmation";
    public static final String SWG_AUTH_REGISTER_OPERATION = "Register a new user in the system";
    public static final String SWG_AUTH_REGISTER_MESSAGE = "User registered successfully!";
    public static final String SWG_AUTH_LOGIN_OPERATION = "Authenticate an user";
    public static final String SWG_AUTH_LOGIN_MESSAGE = "Authenticated successfully!";
    public static final String SWG_AUTH_LOGIN_ERROR = "Bad credentials | The account is deactivated | The account isn't confirmed yet";
    public static final String SWG_AUTH_CONFIRM_ACCOUNT_OPERATION = "Confirm the account of an user";
    public static final String SWG_AUTH_CONFIRM_ACCOUNT_MESSAGE = "Account confirmed successfully!";
    public static final String SWG_AUTH_CONFIRM_ACCOUNT_ERROR = "The token is invalid | The token has been expired";

    public static final String SWG_RESPWD_TAG_NAME = "Password Reset";
    public static final String SWG_RESPWD_TAG_DESCRIPTION = "Operations pertaining to user's reset password process";
    public static final String SWG_RESPWD_FORGOT_OPERATION = "Request a link to reset the password";
    public static final String SWG_RESPWD_FORGOT_MESSAGE = "Reset link sent to the mail box successfully!";
    public static final String SWG_RESPWD_FORGOT_ERROR = "No user found with the email provided";
    public static final String SWG_RESPWD_RESET_OPERATION = "Change the user password through a reset token";
    public static final String SWG_RESPWD_RESET_MESSAGE = "The action completed successfully!";
    public static final String SWG_RESPWD_RESET_ERROR = "The token is invalid or has expired";

    public static final String SWG_TOKEN_TAG_NAME = "Token Operation";
    public static final String SWG_TOKEN_TAG_DESCRIPTION = "Operations pertaining to token validation or refresh";
    public static final String SWG_TOKEN_VALIDATE_OPERATION = "Validate a token";
    public static final String SWG_TOKEN_VALIDATE_MESSAGE = "The token is valid";
    public static final String SWG_TOKEN_VALIDATE_ERROR = "Invalid token | The token has expired";
    public static final String SWG_TOKEN_REFRESH_OPERATION = "Refresh token by generating new one";
    public static final String SWG_TOKEN_REFRESH_MESSAGE = "New access token generated successfully";
    public static final String SWG_TOKEN_REFRESH_ERROR = "Invalid token | The token is unallocated";

    public static final String SWG_USER_TAG_NAME = "Users";
    public static final String SWG_USER_TAG_DESCRIPTION = "Operations pertaining to user's update, retrieve and delete";
    public static final String SWG_USER_LIST_OPERATION = "Get all users";
    public static final String SWG_USER_LIST_MESSAGE = "List retrieved successfully!";
    public static final String SWG_USER_LOGGED_OPERATION = "Get the authenticated user";
    public static final String SWG_USER_LOGGED_MESSAGE = "User retrieved successfully!";
    public static final String SWG_USER_ITEM_OPERATION = "Get one user";
    public static final String SWG_USER_ITEM_MESSAGE = "Item retrieved successfully!";
    public static final String SWG_USER_UPDATE_OPERATION = "Update an user";
    public static final String SWG_USER_UPDATE_MESSAGE = "User updated successfully!";
    public static final String SWG_USER_UPDATE_PWD_OPERATION = "Update user password";
    public static final String SWG_USER_UPDATE_PWD_MESSAGE = "The password updated successfully!";
    public static final String SWG_USER_UPDATE_PWD_ERROR = "The current password is invalid";
    public static final String SWG_USER_DELETE_OPERATION = "Delete a user";
    public static final String SWG_USER_DELETE_MESSAGE = "User deleted successfully!";
    public static final String SWG_USER_PICTURE_OPERATION = "Change or delete user picture";
    public static final String SWG_USER_PICTURE_MESSAGE = "The picture updated/deleted successfully!";
    public static final String SWG_USER_PICTURE_ERROR = "An IOException occurred!";

    public static final String SWG_ROLE_TAG_NAME = "Roles";
    public static final String SWG_ROLE_TAG_DESCRIPTION = "Operations pertaining to role creation, update, assign, revoke, retrieve and delete";
    public static final String SWG_ROLE_CREATE_OPERATION = "Create a role";
    public static final String SWG_ROLE_CREATE_MESSAGE = "Role created successfully!";
    public static final String SWG_ROLE_LIST_OPERATION = "Get all users";
    public static final String SWG_ROLE_LIST_MESSAGE = "List retrieved successfully!";
    public static final String SWG_ROLE_ITEM_OPERATION = "Get one user";
    public static final String SWG_ROLE_ITEM_MESSAGE = "Item retrieved successfully!";
    public static final String SWG_ROLE_UPDATE_OPERATION = "Update an user";
    public static final String SWG_ROLE_UPDATE_MESSAGE = "User updated successfully!";
    public static final String SWG_ROLE_DELETE_OPERATION = "Delete a user";
    public static final String SWG_ROLE_DELETE_MESSAGE = "User deleted successfully!";
    public static final String SWG_ROLE_ASSIGN_OPERATION = "Assign roles to an user";
    public static final String SWG_ROLE_ASSIGN_MESSAGE = "Roles successfully assigned to user!";
    public static final String SWG_ROLE_REVOKE_OPERATION = "Revoke roles to an user";
    public static final String SWG_ROLE_REVOKE_MESSAGE = "Roles successfully revoked to user!";
}