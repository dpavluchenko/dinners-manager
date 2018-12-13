package security;

import domain.UserRole;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class SecurityFilter implements Filter {

    private SecurityConfig securityConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.securityConfig = SecurityConfig
                .create()
                .permitAll("/api/login", "/api/logout", "/api/user/register")
                .authenticated("/api", "/api/manage/*")
                .hasRole(UserRole.USER, "/api")
                .hasRole(UserRole.MANAGER, "/api/manage/*")
                .build();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        PermissionStatus status = securityConfig.checkUserPermission(req);

        if (status.equals(PermissionStatus.OK)) chain.doFilter(req, res);
        else res.sendError(status.code);
    }

    @Override
    public void destroy() {}
}
