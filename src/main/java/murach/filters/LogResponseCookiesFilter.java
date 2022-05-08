package murach.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter(filterName = "LogResponseCookiesFilter"/*,
        urlPatterns = "/*",
        dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD}*/)
public class LogResponseCookiesFilter implements Filter {
    private FilterConfig filterConfig = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        ResponseCookiesWrapper wrappedResponse = new ResponseCookiesWrapper(httpResponse);

        chain.doFilter(request, response);

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        ServletContext sc = filterConfig.getServletContext();
        String filterName = filterConfig.getFilterName();
        String servletPath = "Servlet path: " + httpRequest.getServletPath();
        List<Cookie> cookies = wrappedResponse.getCookies();
        String cookiesString = "";
        for (Cookie c : cookies)
            cookiesString += c.getName() + "=" + c.getValue() + " ";
        sc.log(filterName + " | " + servletPath + " | cookies: " + cookiesString);
    }

    public void destroy() {
        filterConfig = null;
    }

    class ResponseCookiesWrapper extends HttpServletResponseWrapper {
        private List<Cookie> cookies = null;

        public ResponseCookiesWrapper(HttpServletResponse response) {
            super(response);
            cookies = new ArrayList<>();
        }

        public List<Cookie> getCookies() {
            return cookies;
        }

        public void addCookie(Cookie cookie) {
            cookies.add(cookie);
            HttpServletResponse httpResponse = (HttpServletResponse) this.getResponse();
            httpResponse.addCookie(cookie);
        }
    }
}
