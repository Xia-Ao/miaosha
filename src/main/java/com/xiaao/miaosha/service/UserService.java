package com.xiaao.miaosha.service;
/**
 * @author: Xia-ao
 * @create: 2019-01-22 22:11
 **/

import com.xiaao.miaosha.error.BusinessException;
import com.xiaao.miaosha.service.model.UserModel;

/**
 * @Interface: UserServiceImpl
 * @description: NULL
 * @author: Xia-ao
 * @create: 2019-01-22 22:11
 **/
public interface UserService {

    public UserModel getUserById(Integer id);

    public void register(UserModel userModel) throws BusinessException;

    public UserModel validateLogin(String telephone, String encrptPassword) throws BusinessException;
}
