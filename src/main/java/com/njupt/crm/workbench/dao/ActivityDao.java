package com.njupt.crm.workbench.dao;

import com.njupt.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int saveActivity(Activity activity);

    int pageListNum(Map<String, Object> map);

    List<Activity> pageList(Map<String, Object> map);

    int getNumDelByids(String[] ids);
}
