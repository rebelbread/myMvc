package com.bread.springmvc.example.dao.impl;

import annotation.Repository;
import com.bread.springmvc.example.dao.UserDao;

/**
 *
 * @Description:
 * @author zhiwj
 * @date 2019-10-21 9:59
 */
@Repository("userDao")
public class UserDaoImpl implements UserDao {

    @Override
    public String getUser(String userId) {
        return "hello:" + userId;
    }
}
