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
    public void init(FilterConfig filterConfig) {
        this.securityConfig = SecurityConfig
                .create()
                .permitAll("/api/login", "/api/logout", "/api/register")
                .authenticated("/api/orders/*", "/api/manage/*")
                .hasRole(UserRole.MANAGER, "/api/manage/*")
                .build();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        int status = securityConfig.checkUserPermission(req);

        if (status == HttpServletResponse.SC_OK) chain.doFilter(req, res);
        else res.setStatus(status);
    }

    @Override
    public void destroy() {}
}
