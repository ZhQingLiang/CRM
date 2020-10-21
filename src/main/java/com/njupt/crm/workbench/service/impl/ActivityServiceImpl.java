package com.njupt.crm.workbench.service.impl;

import com.njupt.crm.settings.dao.UserDao;
import com.njupt.crm.settings.domain.User;
import com.njupt.crm.utils.SqlSessionUtil;
import com.njupt.crm.vo.PaginationVO;
import com.njupt.crm.workbench.dao.ActivityDao;
import com.njupt.crm.workbench.dao.ActivityRemarkDao;
import com.njupt.crm.workbench.domain.Activity;
import com.njupt.crm.workbench.domain.ActivityRemark;
import com.njupt.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    @Override
    public boolean saveActivity(Activity activity) {
        boolean flag = false;
        int count = activityDao.saveActivity(activity);

        if (count==1){
            flag = true;
        }
        System.out.println(flag);
        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {
        int num = activityDao.pageListNum(map);
        List<Activity> list= activityDao.pageList(map);
        PaginationVO<Activity> paginationVO = new PaginationVO<Activity>();
        paginationVO.setTotal(num);
        paginationVO.setDataList(list);
        return paginationVO;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;
        int activityRemarkNum = activityRemarkDao.getNumByids(ids);
        int activityRemarkDel = activityRemarkDao.getNumDelByids(ids);
        if(activityRemarkDel!=activityRemarkNum){
            flag = false;
        }
        int activityDel = activityDao.getNumDelByids(ids);
        if(activityDel!=ids.length){
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> editList(String id) {
        List<User> list = userDao.getUserList();
        Activity activity = activityDao.getActivity(id);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("owners",list);
//        ???
        map.put("activity",activity);
        return map;
    }

    @Override
    public boolean update(Activity activity) {
        int count= activityDao.update(activity);
        boolean flag = true;
        if(count!=1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Activity detail(String id) {
//        ??owner变成32长度,只是String变化，数据库长度没变
        Activity activity = activityDao.detail(id);
        return activity;
    }

    @Override
    public ActivityRemark[] showRemark(String id) {
        ActivityRemark[] activityRemark = activityRemarkDao.showRemark(id);
        return activityRemark;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = false;
        int count = activityRemarkDao.deleteRemark(id);
        if(count==1){
            flag=true;
        }
        return flag;
    }

    @Override
    public boolean editRemark(ActivityRemark activityRemark) {
        boolean flag = false;
        int count = activityRemarkDao.editRemark(activityRemark);
        if(count==1){
            flag=true;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark activityRemark) {
        boolean flag = false;
        int count = activityRemarkDao.saveRemark(activityRemark);
        if(count==1){
            flag=true;
        }
        return flag;
    }


}
