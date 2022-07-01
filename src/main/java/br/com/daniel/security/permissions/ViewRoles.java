package br.com.daniel.security.permissions;

import java.util.HashSet;
import java.util.Set;

public class ViewRoles {
    public static Set<String> USERS_ROOT_ROLES;
    public static Set<String> SERVICE_DESK_ROOT_ROLES;
    public static Set<String> CLIENT_ROOT_ROLES;

    static {
        USERS_ROOT_ROLES = new HashSet<>();
        USERS_ROOT_ROLES.add("ADMIN");

        SERVICE_DESK_ROOT_ROLES = new HashSet<>();
        SERVICE_DESK_ROOT_ROLES.add("SERVICE_DESK");

        CLIENT_ROOT_ROLES = new HashSet<>();
        CLIENT_ROOT_ROLES.add("CLIENT");
    }

    private ViewRoles() {
    }
}
