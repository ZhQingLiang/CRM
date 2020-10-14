package com.njupt.crm.web.filter;

import com.njupt.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入登录过滤器");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String path = request.getServletPath();
        if ("/settings/user/login.do".equals(path)||"/login.jsp".equals(path)){
            filterChain.doFilter(request,response);
        }else {
            if(user==null){
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }else {
                filterChain.doFilter(request,response);
            }
        }


    }

    @Override
    public void destroy() {

    }
}
