package com.njupt.crm.settings.service.impl;

import com.njupt.crm.exception.LoginException;
import com.njupt.crm.settings.dao.UserDao;
import com.njupt.crm.settings.domain.User;
import com.njupt.crm.settings.service.UserService;
import com.njupt.crm.utils.DateTimeUtil;
import com.njupt.crm.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private UserDao  userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String,String> map = new HashMap<String, String>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user = userDao.login(map);

        if(user==null){
            throw new LoginException("账号密码错误");
        }

        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if(expireTime.compareTo(currentTime)<0){
            throw new LoginException("账号失效");
        }

        String lockStatement = user.getLockState();
        if ("0".equals(lockStatement)){
            throw new LoginException("账号锁定");
        }

        String allowip = user.getAllowIps();
        if(!allowip.contains(ip)){
            throw new LoginException("IP无效");
        }

        return user;
    }

    @Override
    public List<User> getUserList() {
        List<User> userList = userDao.getUserList();
        return userList;
    }
}
