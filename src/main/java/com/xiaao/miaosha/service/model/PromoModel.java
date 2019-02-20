package com.xiaao.miaosha.service.model;
/**
 * @author: Xia-ao
 * @create: 2019-02-17 20:56
 **/

import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * @className: PromoModel
 * @description: NULL
 * @author: Xia-ao
 * @create: 2019-02-17 20:56
 **/
public class PromoModel {

    // 秒杀活动id
    private Integer id;


    // 秒杀活动状态
    private Integer status;

    // 秒杀活动开始时间
    // 使用joda -time处理时间
    private DateTime startTime ;

    // 秒杀活动结束时间
    private DateTime endTime;

    // 秒杀活动使用的商品id
    private Integer itemId;

    // 秒杀活动的商品价格
    private BigDecimal promoItemPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getPromoItemPrice() {
        return promoItemPrice;
    }

    public void setPromoItemPrice(BigDecimal promoItemPrice) {
        this.promoItemPrice = promoItemPrice;
    }
}
