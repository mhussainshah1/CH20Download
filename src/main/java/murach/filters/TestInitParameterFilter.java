package murach.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebFilter(filterName = "TestInitParameterFilter",
        urlPatterns = {"/*"},
        initParams = {
                @WebInitParam(name = "logFilename",
                        value = "test_init_params.log")
        })
public class TestInitParameterFilter implements Filter {
    private FilterConfig filterConfig = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        ServletContext sc = filterConfig.getServletContext();
        String filterName = filterConfig.getFilterName();
        String logFilename = filterConfig.getInitParameter("logFilename");
        sc.log(filterName + " | logFilename: " + logFilename);
        chain.doFilter(request, response);
    }

    public void destroy() {
        filterConfig = null;
    }
}
