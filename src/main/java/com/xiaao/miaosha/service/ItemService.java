package com.xiaao.miaosha.service;
/**
 * @author: Xia-ao
 * @create: 2019-02-11 20:43
 **/

import com.xiaao.miaosha.error.BusinessException;
import com.xiaao.miaosha.service.model.ItemModel;

import java.util.List;

/**
 * @Interface: ItemService
 * @description: 商品服务
 * @author: Xia-ao
 * @create: 2019-02-11 20:43
 **/
public interface ItemService {

    // 创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    // 商品列表浏览
    List<ItemModel> listItem();

    // 商品详情
    ItemModel getItemById(Integer id);


    // 减库存
    Boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException;

    // 下单成功，增加商品数量
    void increaseSales(Integer itemId, Integer amount) throws BusinessException;
}
