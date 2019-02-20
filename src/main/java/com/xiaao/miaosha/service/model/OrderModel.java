package com.xiaao.miaosha.service.model;
/**
 * @author: Xia-ao
 * @create: 2019-02-14 12:39
 **/

import java.math.BigDecimal;

/**
 * @className: OrderModel
 * @description: 用户订单交易数据模型
 * @author: Xia-ao
 * @create: 2019-02-14 12:39
 **/
public class OrderModel {

    //String类型的交易号（订单号）
    //201812023349823723 一般可以以时间开头，后面每一位都有特殊含义
    private String id;

    // 用户id
    private Integer userId;

    // 商品id
    private Integer itemId;

    // 购买数量
    private Integer amount;

    //购买的金额（商品下单时的单价X数量）
    //商品的价格是浮动的，后期价格会改变，但创建订单时的价格是不随后期实际价格改变的
    private BigDecimal orderPrice;

    // 创建订单时的单价
    // 若promoid为空，则表示秒杀商品价格
    private BigDecimal itemPrice;

    // 秒杀活动id
    //若非空，则表示以秒杀方式下单
    private Integer promoId;

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }
}

