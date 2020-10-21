package com.njupt.crm.workbench.dao;

import com.njupt.crm.workbench.domain.ActivityRemark;

public interface ActivityRemarkDao {
    int getNumByids(String[] ids);

    int getNumDelByids(String[] ids);

    ActivityRemark[] showRemark(String id);

    int deleteRemark(String id);

    int editRemark(ActivityRemark id);

    int saveRemark(ActivityRemark activityRemark);
}
