package com.hielfsoft.volunteercrowd.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String GROUP = "ROLE_GROUP";

    public static final String SINGLE = "ROLE_SINGLE";

    public static final String APPUSER = "ROLE_APPUSER";

    private AuthoritiesConstants() {
    }
}
