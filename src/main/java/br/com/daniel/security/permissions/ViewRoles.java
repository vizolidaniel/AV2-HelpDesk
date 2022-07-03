package br.com.daniel.security.permissions;

import br.com.daniel.security.domain.UserPrincipal;
import br.com.daniel.utils.Principal;

import java.util.HashSet;
import java.util.Set;

public class ViewRoles {
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
        return isHelper() || isClient();
    }

    public static boolean canCreateRequests() {
        return isClient() && !isAdmin();
    }
}
