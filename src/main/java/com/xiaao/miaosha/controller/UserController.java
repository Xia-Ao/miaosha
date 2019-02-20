package com.xiaao.miaosha.controller;
/**
 * @author: Xia-ao
 * @create: 2019-01-22 22:08
 **/

import com.alibaba.druid.util.StringUtils;
import com.xiaao.miaosha.controller.viewObject.UserVo;
import com.xiaao.miaosha.dataobject.UserPasswordDo;
import com.xiaao.miaosha.error.BusinessException;
import com.xiaao.miaosha.error.EmBusinessErr;
import com.xiaao.miaosha.response.CommonReturnType;
import com.xiaao.miaosha.service.UserService;
import com.xiaao.miaosha.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;
import sun.security.provider.MD5;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @className: UserController
 * @description: 用户接口
 * @author: Xia-ao
 * @create: 2019-01-22 22:08
 **/

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController extends BaseController {


    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;


    /**
     * @Description: // 获取opt短信接口
     **/
    @RequestMapping(value = "/getopt", method = {RequestMethod.POST, RequestMethod.OPTIONS}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOpt(@RequestParam(name = "tel") String telephone) {
        // 1.按照一定的规则生成opt验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);  // 取0-99999之间的随机数
        randomInt += 10000; //随机数在10000-10999之间
        String optCode = String.valueOf(randomInt);

        // 2.将opt验证码与统一手机的手机号关联
        // 一般采取key value对的方式，key是手机号，value是生成码
        // 一般都放到redis中，天生是kv结构，而且相同key覆盖value，还能指定有效期。
        // 这里使用session绑定手机号和optcode
        httpServletRequest.getSession().setAttribute(telephone, optCode);
        System.out.println("telephone=" + telephone + "$OptCode=" + optCode);

        // 3.将opt验证码通过短信通道发送给用户，这一步需要第三方的短信服务商 省略

        return CommonReturnType.create(null);
    }

    /**
     * @Description: // 用户注册接口
     **/
    @RequestMapping(value = "/register", method = {RequestMethod.POST, RequestMethod.OPTIONS}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "name") String name,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "gender") Byte gender,
                                     @RequestParam(name = "telephone") String telephone,
                                     @RequestParam(name = "optCode") String optCode,
                                     @RequestParam(name = "password") String password)
            throws BusinessException, NoSuchAlgorithmException, UnsupportedEncodingException {

        // 1. 验证手机号与验证码是否符合
        //在生成otpcode时，将验证码放入了session中，根据注册时传入的手机号，取对应的otpCode
        String inSessionOptCode = (String) this.httpServletRequest.getSession().getAttribute(telephone);
        // 使用alibaba的druid中的stringUtils类，equals中有判空处理
        if (!StringUtils.equals(optCode, inSessionOptCode)) {
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDATION_ERROR, "短信验证码不正确");
        }

        // 2. 用户注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setAge(age);
        userModel.setGender(gender);
        userModel.setTelphone(telephone);
        userModel.setRegisterMode("byPhone");
        // 对密码进行加密
        userModel.setEncrptPassword(this.EnCodeByMD5(password));
        userService.register(userModel);

        return CommonReturnType.create(null);
    }

    /**
     * @Description: // 用户登录接口
     **/
    @RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.OPTIONS}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(
            @RequestParam(name = "telephone") String telephone,
            @RequestParam(name = "password") String password)
            throws BusinessException, NoSuchAlgorithmException, UnsupportedEncodingException {
        // 1.入参校验
        if (org.apache.commons.lang3.StringUtils.isEmpty(telephone)
                || org.apache.commons.lang3.StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDATION_ERROR);
        }
        // 2. 用户登录服务，验证用户登录是否合法
        UserModel userModel = userService.validateLogin(telephone, this.EnCodeByMD5(password));

        // 3. 将登录凭证加入到用户登录成功的凭证中
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);

        return CommonReturnType.create(null);
    }


    /**
     * @Description: // 通过id查询用户
     **/
    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        // 调用service服务获取对应id对象返回给前端

        UserModel userModel = userService.getUserById(id);

        if (userModel == null) {
            throw new BusinessException(EmBusinessErr.USER_NOT_EXIST);
        }

        // 将核心领域的用户模型对象转化为可供前端使用的viewObject
        UserVo userVo = convertFromUserModel(userModel);
        return CommonReturnType.create(userVo);
    }


    // MD5加密方法
    public String EnCodeByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();

        // 加密 字符
        String newStr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newStr;
    }

    // Usermodel 转化为useVo
    private UserVo convertFromUserModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userModel, userVo);
        return userVo;
    }


}
