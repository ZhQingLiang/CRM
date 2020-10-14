package com.njupt.crm.workbench.service.impl;

import com.njupt.crm.utils.SqlSessionUtil;
import com.njupt.crm.workbench.dao.ActivityDao;
import com.njupt.crm.workbench.domain.Activity;
import com.njupt.crm.workbench.service.ActivityService;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
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
}
