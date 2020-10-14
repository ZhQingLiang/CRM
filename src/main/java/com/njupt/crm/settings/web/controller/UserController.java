package com.njupt.crm.settings.web.controller;

import com.njupt.crm.exception.LoginException;
import com.njupt.crm.settings.domain.User;
import com.njupt.crm.settings.service.UserService;
import com.njupt.crm.settings.service.impl.UserServiceImpl;
import com.njupt.crm.utils.MD5Util;
import com.njupt.crm.utils.PrintJson;
import com.njupt.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入用户控制器");
//        获取url-pattern
        String path = request.getServletPath();
//        注意有/ !!!!!!!!!!!
        if("/settings/user/login.do".equals(path)){
            login(request,response);
        }else if("/settings/user/xxx.do".equals(path)){
//            xxx(request,response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        loginPwd = MD5Util.getMD5(loginPwd);
        String ip = request.getRemoteAddr();
        System.out.println("ip="+ip);


//        WHY???
//        走事务，使用代理实现类，便于以后功能扩展，传zs取ls
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        User user = null;
        try {
            user = us.login(loginAct,loginPwd,ip);
//            下行与PrintJson无关
            request.getSession().setAttribute("user",user);
            PrintJson.printJsonFlag(response,true);
        } catch (LoginException e) {
            e.printStackTrace();
            String msg = e.getMessage();

            Map<String,Object> map = new HashMap<String, Object>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
        }


    }
}
