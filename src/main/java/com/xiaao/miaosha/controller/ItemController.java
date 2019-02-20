package com.xiaao.miaosha.controller;
/**
 * @author: Xia-ao
 * @create: 2019-02-11 21:24
 **/

import com.xiaao.miaosha.controller.viewObject.ItemVo;
import com.xiaao.miaosha.error.BusinessException;
import com.xiaao.miaosha.response.CommonReturnType;
import com.xiaao.miaosha.service.ItemService;
import com.xiaao.miaosha.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @className: ItemController
 * @description: 商品控制层
 * @author: Xia-ao
 * @create: 2019-02-11 21:24
 **/
@Controller("item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    /**
     * @Description: // 创建商品接口
     **/
    @RequestMapping(value = "/create", method = {RequestMethod.POST, RequestMethod.OPTIONS}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name = "title") String title,
                                       @RequestParam(name = "description") String description,
                                       @RequestParam(name = "price") BigDecimal price,
                                       @RequestParam(name = "stock") Integer stock,
                                       @RequestParam(name = "imgUrl") String imgUrl) throws BusinessException {
        //封装service请求用来创建商品
        //尽量让Controller层简单，让Service层负责，把服务逻辑尽可能聚合在Service层内部，实现流转处理
        //创建给service层的
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setStock(stock);
        itemModel.setPrice(price);
        itemModel.setImgUrl(imgUrl);

        // 创建商品，返回itemModel
        ItemModel itemModelReturn = itemService.createItem(itemModel);

        // 返回view层数据给前端
        ItemVo itemVo = convertVoFromModel(itemModel);

        return CommonReturnType.create(itemVo);
    }


    /**
     * @Description: // 通过商品Id获取商品详情
     **/
    @RequestMapping(value = "/getItem", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "id") Integer id) {
        ItemModel itemModel = itemService.getItemById(id);

        ItemVo itemVo = convertVoFromModel(itemModel);


        return CommonReturnType.create(itemVo);
    }


    /**
     * @Description: // 获取商品列表
     **/
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType itemList() {
        List<ItemModel> itemModelList = itemService.listItem();

        // 将model转化为vo 集合
        List<ItemVo> itemVoList = itemModelList.stream().map(itemModel -> {
            ItemVo itemVo = this.convertVoFromModel(itemModel);
            return itemVo;
        }).collect(Collectors.toList());


        return CommonReturnType.create(itemVoList);
    }


    private ItemVo convertVoFromModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemVo itemVo = new ItemVo();
        BeanUtils.copyProperties(itemModel, itemVo);

        // 判断是否存在秒杀活动，存在的话将秒杀活动信息传递给itemVo
        if (itemModel.getPromoModel() != null) {
            itemVo.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVo.setPromoId(itemModel.getPromoModel().getId());
            itemVo.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
            itemVo.setStartTime(itemModel.getPromoModel().getStartTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVo.setEndTime(itemModel.getPromoModel().getEndTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        }

        return itemVo;
    }
}
