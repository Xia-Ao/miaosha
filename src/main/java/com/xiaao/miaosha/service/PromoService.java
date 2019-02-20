package com.xiaao.miaosha.service;
/**
 * @author: Xia-ao
 * @create: 2019-02-17 21:12
 **/

import com.xiaao.miaosha.service.model.PromoModel;

/**
 * @Interface: PromoService
 * @description: Promo秒杀活动service
 * @author: Xia-ao
 * @create: 2019-02-17 21:12
 **/
public interface PromoService {

    // 根据商品itemid获取商品秒杀活动信息
    PromoModel getPromoByItemId(Integer itemId);

    //判断当前时间是否秒杀活动即将开始或正在进行

}
