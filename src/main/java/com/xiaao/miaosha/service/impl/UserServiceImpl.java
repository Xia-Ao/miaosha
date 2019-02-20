package com.xiaao.miaosha.service.impl;
/**
 * @author: Xia-ao
 * @create: 2019-01-22 22:09
 **/

import com.xiaao.miaosha.dao.UserDoMapper;
import com.xiaao.miaosha.dao.UserPasswordDoMapper;
import com.xiaao.miaosha.dataobject.UserDo;
import com.xiaao.miaosha.dataobject.UserPasswordDo;
import com.xiaao.miaosha.error.BusinessException;
import com.xiaao.miaosha.error.EmBusinessErr;
import com.xiaao.miaosha.service.UserService;
import com.xiaao.miaosha.service.model.UserModel;
import com.xiaao.miaosha.validate.ValidationResult;
import com.xiaao.miaosha.validate.ValidatorImpl;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * @className: UserServiceImpl
 * @description: NULL
 * @author: Xia-ao
 * @create: 2019-01-22 22:09
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDoMapper userDoMapper;

    @Autowired
    private UserPasswordDoMapper userPasswordDoMapper;

    @Autowired
    private ValidatorImpl validator;

    // 用户通过id查询实现
    @Override
    public UserModel getUserById(Integer id) {
        // 调用userDoMapper获取到对应的用户
        UserDo userDo = userDoMapper.selectByPrimaryKey(id);

        if (userDo == null) {
            return null;
        }
        UserPasswordDo userPasswordDo = userPasswordDoMapper.selectByUserId(userDo.getId());
        return convertFormDataObject(userDo, userPasswordDo);
    }


    // 用户注册实现
    @Override
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDATION_ERROR);
        }

//        // 对注册提交的字段进行校验
//        if (StringUtils.isEmpty(userModel.getName())
//                || userModel.getAge() == null
//                || userModel.getGender() == null
//                || StringUtils.isEmpty(userModel.getTelphone())) {
//            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDATION_ERROR);
//        }

        // 使用validator的方式对传递过来的参数进行校验，不再使用上面一个一个校验的方式，
        ValidationResult validationResult = validator.validate(userModel);
        // 验证userModel，如果有错误，在validate中会将结果中的hasError置为true
        if (validationResult.isHasErrors()) {
            // 有错误，抛出异常，处理错误信息
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDATION_ERROR, validationResult.getErrMsg());
        }

        // 实现model->dataobject方法
        UserDo userDo = convertFromModel(userModel);
        try {
            userDoMapper.insertSelective(userDo);
        } catch (DuplicateKeyException ex) {
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDATION_ERROR, "改手机号已经注册");
        }

        userModel.setId(userDo.getId());

        UserPasswordDo userPasswordDo = convertPasswordFromModel(userModel);
        userPasswordDoMapper.insertSelective(userPasswordDo);
    }

    /**
     * @Description: //用户登录校验，比对账号密码
     **/
    @Override
    public UserModel validateLogin(String telephone, String encrptPassword) throws BusinessException {
        // 1.通过手机号获取用户信息
        UserDo userDo = userDoMapper.selectByTelphone(telephone);
        if (userDo == null) {
            throw new BusinessException(EmBusinessErr.USER_LOGIN_FAIL);
        }
        UserPasswordDo userPasswordDo = userPasswordDoMapper.selectByUserId(userDo.getId());

        UserModel userModel = convertFormDataObject(userDo, userPasswordDo);

        // 2.比对加密密码是否相等互相匹配
        if (!StringUtils.equals(encrptPassword, userModel.getEncrptPassword())) {
            throw new BusinessException(EmBusinessErr.USER_LOGIN_FAIL);
        }

        return userModel;
    }

    private UserDo convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserDo userDo = new UserDo();
        BeanUtils.copyProperties(userModel, userDo);

        return userDo;
    }

    private UserPasswordDo convertPasswordFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserPasswordDo userPasswordDo = new UserPasswordDo();
        userPasswordDo.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDo.setUserId(userModel.getId());
        return userPasswordDo;
    }

    private UserModel convertFormDataObject(UserDo userDo, UserPasswordDo userPasswordDo) {
        if (userDo == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDo, userModel);
        // 拷贝完成之后还有复制密码
        if (userPasswordDo != null) {
            userModel.setEncrptPassword(userPasswordDo.getEncrptPassword());
        }
        return userModel;       // 将这个userModel返回
    }


}
