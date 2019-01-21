package security;

import config.ApplicationProperties;
import domain.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SecurityConfig {

    private final List<RouteMatch> permittedRoutes;
    private final List<RouteMatch> authenticatedRoutes;
    private final Map<UserRole, List<RouteMatch>> routesByRole;

    private final String userAttrName = ApplicationProperties.getInstance().getProperty("session.user.key");

    private SecurityConfig(Builder builder) {
        this.permittedRoutes = builder.permittedRoutes;
        this.authenticatedRoutes = builder.authenticatedRoutes;
        this.routesByRole = builder.routesByRole;
    }

    public int checkUserPermission(HttpServletRequest request) {
        String route = request.getRequestURI();

        if (matchesPattern(permittedRoutes, route)) return HttpServletResponse.SC_OK;

        HttpSession session = request.getSession(false);

        if (matchesPattern(authenticatedRoutes, route)){
           if (session == null) return HttpServletResponse.SC_UNAUTHORIZED;
           else {
               UserRole userRole = matchesRoleByPattern(routesByRole, route);
               if (userRole == null){
                   return HttpServletResponse.SC_OK;
               } else {
                   UserDetails userDetails = (UserDetails) session.getAttribute(userAttrName);
                   return userDetails.getRole().equals(userRole) ? HttpServletResponse.SC_OK : HttpServletResponse.SC_FORBIDDEN;
               }
           }
        }

        return HttpServletResponse.SC_NOT_FOUND;
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
