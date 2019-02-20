package com.xiaao.miaosha.service.impl;
/**
 * @author: Xia-ao
 * @create: 2019-02-17 21:15
 **/

import com.xiaao.miaosha.dao.PromoDoMapper;
import com.xiaao.miaosha.dataobject.PromoDo;
import com.xiaao.miaosha.service.PromoService;
import com.xiaao.miaosha.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @className: PromoServiceImpl
 * @description:
 * @author: Xia-ao
 * @create: 2019-02-17 21:15
 **/
@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoDoMapper promoDoMapper;


    @Override
    public PromoModel getPromoByItemId(Integer itemId) {

        // 1. 获取当前商品的秒杀信息
        PromoDo promoDo = promoDoMapper.selectByItemId(itemId);

        // 2. 将Do转化为Model
        PromoModel promoModel = convertPromoDoFromProDo(promoDo);

        // 3. 判断秒杀活动是否存在
        if (promoModel == null) {
            return null;
        }

        // 4. 存在秒杀活动，则判断当前时间与秒杀活动开始时间的关系
        // 活动开始时间在当前时间之后，
        if (promoModel.getStartTime().isAfterNow()) {
            // 活动还未开始
            promoModel.setStatus(1);
        } else if (promoModel.getEndTime().isBeforeNow()) {
            // 活动已经结束
            promoModel.setStatus(3);
        } else {
            // 活动正在进行中
            promoModel.setStatus(2);
        }

        return promoModel;
    }

    private PromoModel convertPromoDoFromProDo(PromoDo promoDo) {
        if (promoDo == null) {
            return null;
        }

        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDo, promoModel);
        // 单独设置价格 价格在数据库中是double，model中使用的是BigDecimal
        promoModel.setPromoItemPrice(new BigDecimal(promoDo.getPromoItemPrice()));
        // 单独设置时间 时间在数据库中是sql.date ,model中是joda-time
        promoModel.setStartTime(new DateTime(promoDo.getStartTime()));
        promoModel.setEndTime(new DateTime(promoDo.getEndTime()));

        return promoModel;
    }
}
