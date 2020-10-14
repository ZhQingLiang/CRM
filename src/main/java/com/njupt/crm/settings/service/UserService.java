package com.njupt.crm.settings.service;

import com.njupt.crm.exception.LoginException;
import com.njupt.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String name, String password, String ip) throws LoginException;

    List<User> getUserList();
}
