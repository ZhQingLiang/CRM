package com.njupt.crm.workbench.service;

import com.njupt.crm.vo.PaginationVO;
import com.njupt.crm.workbench.domain.Activity;
import com.njupt.crm.workbench.domain.ActivityRemark;

import java.util.Map;

public interface ActivityService {
    boolean saveActivity(Activity activity);

    PaginationVO<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> editList(String id);

    boolean update(Activity activity);

    Activity detail(String id);

    ActivityRemark[] showRemark(String id);

    boolean deleteRemark(String id);
}
