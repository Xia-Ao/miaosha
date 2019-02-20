package com.xiaao.miaosha.service;
/**
 * @author: Xia-ao
 * @create: 2019-02-14 17:59
 **/

import com.xiaao.miaosha.error.BusinessException;
import com.xiaao.miaosha.service.model.OrderModel;

/**
 * @Interface: OrderService
 * @description: 订单服务
 * @author: Xia-ao
 * @create: 2019-02-14 17:59
 **/
public interface OrderService {

    /**
     * 创建订单,需要用户的id，商品的id，商品的数量
     * <p>
     * 1 通过前端url上传过来秒杀活动id，下单接口内校验对应id是否属于对应商品且活动已经开始
     * 2 直接在下单接口内判断对应的商品是否存在秒杀活动，若存在进行中的则以秒杀价格下单、
     * 使用第一种，接收id
     */
    OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException;

}
