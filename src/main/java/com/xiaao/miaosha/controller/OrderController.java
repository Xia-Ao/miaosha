package com.xiaao.miaosha.controller;
/**
 * @author: Xia-ao
 * @create: 2019-02-14 21:14
 **/

import com.xiaao.miaosha.error.BusinessException;
import com.xiaao.miaosha.error.EmBusinessErr;
import com.xiaao.miaosha.response.CommonReturnType;
import com.xiaao.miaosha.service.OrderService;
import com.xiaao.miaosha.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @className: OrderController
 * @description: 交易订单CTRL
 * @author: Xia-ao
 * @create: 2019-02-14 21:14
 **/


@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;


    /**
     * @Description: //创建订单接口
     **/
    @RequestMapping(value = "/createOrder", method = {RequestMethod.POST, RequestMethod.OPTIONS}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name = "itemId") Integer itemId,
                                        @RequestParam(name = "amount") Integer amount,
                                        @RequestParam(name = "promoId") Integer promoId) throws BusinessException {

        // 判断用户是否登录，从session中拿出用户信息
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");

        if (isLogin == null || !isLogin.booleanValue()) {
            throw new BusinessException(EmBusinessErr.USER_NOT_LOGIN);
        }
        // 如果用户已经登录，获取用户登录信息
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");

        orderService.createOrder(userModel.getId(), itemId, promoId, amount);

        return CommonReturnType.create(null);

    }

}
