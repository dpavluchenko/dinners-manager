package security;

import domain.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SecurityConfig {

    private final List<RouteMatch> permittedRoutes;
    private final List<RouteMatch> authenticatedRoutes;
    private final Map<UserRole, List<RouteMatch>> routesByRole;

    private final String userAttrName = "userDetails";

    private SecurityConfig(Builder builder) {
        this.permittedRoutes = builder.permittedRoutes;
        this.authenticatedRoutes = builder.authenticatedRoutes;
        this.routesByRole = builder.routesByRole;
    }

    public PermissionStatus checkUserPermission(HttpServletRequest request) {
        String route = request.getServletPath();

        if (matchesPattern(permittedRoutes, route)) return PermissionStatus.OK;

        HttpSession session = request.getSession(false);

        if (matchesPattern(authenticatedRoutes, route)){
           if (session == null) return PermissionStatus.UNAUTHORIZED;
           else {
               UserRole userRole = matchesRoleByPattern(routesByRole, route);
               if (userRole == null){
                   return PermissionStatus.OK;
               } else {
                   UserDetails userDetails = (UserDetails) session.getAttribute(userAttrName);
                   return userDetails.getRole().equals(userRole) ? PermissionStatus.OK : PermissionStatus.FORBIDDEN;
               }
           }
        }

        return PermissionStatus.NOT_FOUND;
    }

    private boolean matchesPattern(List<RouteMatch> routeMatches,String pattern){
        return routeMatches.stream().anyMatch(route -> route.matches(pattern));
    }

    private UserRole matchesRoleByPattern(Map<UserRole, List<RouteMatch>> routeMatches, String pattern){
        UserRole userRole = null;
        for (Map.Entry<UserRole, List<RouteMatch>> entry : routeMatches.entrySet()) {
            if (matchesPattern(entry.getValue(), pattern)) {
                userRole = entry.getKey();
                break;
            }
        }
        return userRole;
    }

    public static Builder create() {
        return new Builder();
    }


    static class Builder {
        private List<RouteMatch> permittedRoutes;
        private List<RouteMatch> authenticatedRoutes;
        private Map<UserRole, List<RouteMatch>> routesByRole;

        public Builder() {
            this.permittedRoutes = new ArrayList<>();
            this.authenticatedRoutes = new ArrayList<>();
            this.routesByRole = new EnumMap<>(UserRole.class);
        }

        public Builder permitAll(String... routes) {
            this.permittedRoutes = createRoutes(routes);
            return this;
        }

        public Builder authenticated(String... routes) {
            this.authenticatedRoutes = createRoutes(routes);
            return this;
        }

        public Builder hasRole(UserRole role, String... routes) {
            this.routesByRole.put(role, createRoutes(routes));
            return this;
        }

        public SecurityConfig build() {
            return new SecurityConfig(this);
        }

        private List<RouteMatch> createRoutes(String[] routes){
            List<RouteMatch> matches = new ArrayList<>();
            for (String route : routes) {
                matches.add(new RouteMatch(route));
            }
            return matches;
        }
     }
}
