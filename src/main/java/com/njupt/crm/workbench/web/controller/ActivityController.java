package com.njupt.crm.workbench.web.controller;

import com.njupt.crm.settings.domain.User;
import com.njupt.crm.settings.service.UserService;
import com.njupt.crm.settings.service.impl.UserServiceImpl;
import com.njupt.crm.utils.DateTimeUtil;
import com.njupt.crm.utils.PrintJson;
import com.njupt.crm.utils.ServiceFactory;
import com.njupt.crm.utils.UUIDUtil;
import com.njupt.crm.vo.PaginationVO;
import com.njupt.crm.workbench.domain.Activity;
import com.njupt.crm.workbench.service.ActivityService;
import com.njupt.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Service;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workbench/activity/getUserList.do".equals(path)) {
            getUserList(request, response);
        } else if ("/workbench/activity/saveActivity.do".equals(path)) {
            saveActivity(request, response);
        } else if ("/workbench/activity/pageList.do".equals(path)) {
            pageList(request, response);
        }

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询市场活动信息列表的操作（结合条件查询+分页查询）");
        String pageNo = request.getParameter("pageNo");
        int pageNoNum = Integer.valueOf(pageNo);
        String pageSize = request.getParameter("pageSize");
        int pageSizeNum = Integer.valueOf(pageSize);
        int skipCount = (pageNoNum-1)*pageSizeNum;
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("pageSizeNum",pageSizeNum);
        map.put("skipCount",skipCount);
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        PaginationVO<Activity> paginationVO = activityService.pageList(map);
        PrintJson.printJsonObj(response,paginationVO);


    }

    private void saveActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动添加操作");
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateBy(createBy);
        activity.setCreateTime(createTime);

        boolean flag = activityService.saveActivity(activity);

//        使用PrintJson.printJsonFlag()返回result给ajax
        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行获取用户列表操作");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();
        PrintJson.printJsonObj(response, userList);
    }
}
