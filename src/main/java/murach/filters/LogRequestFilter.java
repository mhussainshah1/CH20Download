package murach.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import murach.util.CookieUtil;

import java.io.IOException;

@WebFilter(filterName = "LogRequestFilter",
        urlPatterns = "/*",
        dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})
public class LogRequestFilter implements Filter {
    private FilterConfig filterConfig = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        ServletContext sc = filterConfig.getServletContext();

        String logString = filterConfig.getFilterName() + " | "
                + "Servlet path: " + httpRequest.getServletPath() + " | ";

        Cookie[] cookies = httpRequest.getCookies();
        String emailAddress = CookieUtil.getCookieValue(cookies, "emailCookie");

        logString += "Email cookie: ";
        if (emailAddress.length() != 0)
            logString += emailAddress;
        else
            logString += "NotFound";
        sc.log(logString);

        chain.doFilter(request, response);
    }

    public void destroy() {
        filterConfig = null;
    }
}
