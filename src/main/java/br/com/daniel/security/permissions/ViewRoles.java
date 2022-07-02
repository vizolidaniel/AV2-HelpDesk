package br.com.daniel.security.permissions;

import br.com.daniel.security.domain.UserPrincipal;
import br.com.daniel.utils.Principal;

import java.util.HashSet;
import java.util.Set;

public class ViewRoles {
    public static Set<String> USERS_ROOT_ROLES;
    public static Set<String> SERVICE_DESK_ROOT_ROLES;
    public static Set<String> CLIENT_ROOT_ROLES;
    public static Set<String> ADMIN_ROOT_ROLES;

    static {
        USERS_ROOT_ROLES = new HashSet<>();
        USERS_ROOT_ROLES.add("ADMIN");

        SERVICE_DESK_ROOT_ROLES = new HashSet<>();
        SERVICE_DESK_ROOT_ROLES.add("SERVICE_DESK");

        CLIENT_ROOT_ROLES = new HashSet<>();
        CLIENT_ROOT_ROLES.add("CLIENT");

        ADMIN_ROOT_ROLES = new HashSet<>();
        ADMIN_ROOT_ROLES.add("ADMIN");
    }

    private ViewRoles() {
    }

    public static boolean isAdmin() {
        UserPrincipal principal = Principal.extract();
        return principal.listRoles().contains("ADMIN");
    }

    public static boolean isHelper() {
        UserPrincipal principal = Principal.extract();
        return principal.listRoles().contains("SERVICE_DESK");
    }

    public static boolean isClient() {
        UserPrincipal principal = Principal.extract();
        return principal.listRoles().contains("CLIENT");
    }

    public static boolean canManageUsers() {
        return isAdmin();
    }

    public static boolean canManageRequests() {
        return isHelper();
    }

    public static boolean canViewRequests() {
        return isHelper();
    }

    public static boolean canViewSelfRequests() {
        return isClient() && !isAdmin();
    }

    public static boolean canCreateRequests() {
        return isClient() && !isAdmin();
    }
}
