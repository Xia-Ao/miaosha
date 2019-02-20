package com.xiaao.miaosha.service.model;
/**
 * @author: Xia-ao
 * @create: 2019-02-09 22:19
 **/

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @className: ItemModel
 * @description: 商品数据模型
 * @author: Xia-ao
 * @create: 2019-02-09 22:19
 **/
public class ItemModel {

    // 商品id
    private Integer id;

    // 商品名称
    @NotBlank(message = "商品名称不能为空")
    private String title;

    // 商品价格
    @NotNull(message = "商品价格不能为空")
    @Min(value = 0, message = "商品价格不能小于0元")
    private BigDecimal price;

    // 商品库存
    @NotNull(message = "商品库存不能为空")
    private Integer stock;

    // 商品描述
    @NotNull(message = "商品描述不能为空")
    private String description;

    // 商品销量
    private Integer sales;

    // 商品描述图片
    @NotNull(message = "商品描述图片不能为空")
    private String imgUrl;
    //

    // 秒杀活动信息
    // 使用聚合模型，如果promModel不为空，则表示其拥有还未结束的秒杀聚合对象
    private PromoModel promoModel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public PromoModel getPromoModel() {
        return promoModel;
    }

    public void setPromoModel(PromoModel promoModel) {
        this.promoModel = promoModel;
    }
}
