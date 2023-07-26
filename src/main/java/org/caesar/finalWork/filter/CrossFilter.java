package org.caesar.finalWork.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CrossFilter implements Filter {

    public CrossFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        //System.out.println("CrossFilter");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        req.setCharacterEncoding("utf-8");
            resp.setContentType("application/json;charset=utf-8");
            resp.setHeader("Access-Control-Allow-Origin", "http://localhost:8081");
            resp.setHeader("Access-Control-Allow-Credentials", "true");
            resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            resp.setHeader("Access-Control-Allow-Headers",
                    "Accept,"
                            + "Origin,"
                            + "No-Cache,"
                            + "X-Requested-With,"
                            + "If-Modified-Since,"
                            + "Pragma,"
                            + "Last-Modified,"
                            + "Cache-Control,"
                            + "Expires,"
                            + "Content-Type,"
                            + "X-E4M-With");

            filterChain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
