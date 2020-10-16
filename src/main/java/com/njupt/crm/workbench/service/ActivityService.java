package com.njupt.crm.workbench.service;

import com.njupt.crm.vo.PaginationVO;
import com.njupt.crm.workbench.domain.Activity;

import java.util.Map;

public interface ActivityService {
    boolean saveActivity(Activity activity);

    PaginationVO<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);
}
