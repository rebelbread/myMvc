package com.bread.springmvc.example.service.impl;

import annotation.Autowrited;
import annotation.Service;
import com.bread.springmvc.example.dao.UserDao;
import com.bread.springmvc.example.service.UserService;

/**
 *
 * @Description:
 * @author zhiwj
 * @date 2019-10-21 9:58
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowrited("userDao")
    private UserDao userDao;

    @Override
    public String getUser(String userId) {
        return userDao.getUser(userId);
    }
}
