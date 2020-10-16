package com.njupt.crm.workbench.service.impl;

import com.njupt.crm.utils.SqlSessionUtil;
import com.njupt.crm.vo.PaginationVO;
import com.njupt.crm.workbench.dao.ActivityDao;
import com.njupt.crm.workbench.dao.ActivityRemarkDao;
import com.njupt.crm.workbench.domain.Activity;
import com.njupt.crm.workbench.domain.ActivityRemark;
import com.njupt.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
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
}
