package com.xiaao.miaosha.service.impl;
/**
 * @author: Xia-ao
 * @create: 2019-02-11 20:45
 **/

import com.xiaao.miaosha.dao.ItemDoMapper;
import com.xiaao.miaosha.dao.ItemStockDoMapper;
import com.xiaao.miaosha.dataobject.ItemDo;
import com.xiaao.miaosha.dataobject.ItemStockDo;
import com.xiaao.miaosha.error.BusinessException;
import com.xiaao.miaosha.error.EmBusinessErr;
import com.xiaao.miaosha.service.ItemService;
import com.xiaao.miaosha.service.PromoService;
import com.xiaao.miaosha.service.model.ItemModel;
import com.xiaao.miaosha.service.model.PromoModel;
import com.xiaao.miaosha.validate.ValidationResult;
import com.xiaao.miaosha.validate.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @className: ItemServiceImpl
 * @description: 商品服务impl
 * @author: Xia-ao
 * @create: 2019-02-11 20:45
 **/

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDoMapper itemDoMapper;

    @Autowired
    private ItemStockDoMapper itemStockDoMapper;

    @Autowired
    private PromoService promoService;

    /**
     * @Description: // 创建商品条目
     **/
    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        // 1.入参校验
        ValidationResult validationResult = validator.validate(itemModel);
        if (validationResult.isHasErrors()) {
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDATION_ERROR, validationResult.getErrMsg());
        }

        // 2.将ItemModel转化为ItemDo
        ItemDo itemDo = convertItemDoFromItemMoldel(itemModel);

        // 3.将ItemDo写入数据库
        itemDoMapper.insertSelective(itemDo);

        // 4. 根据itemModel获取ItemStockDO，并写入数据库
        itemModel.setId(itemDo.getId());
        ItemStockDo itemStockDo = convertItemStockDoFormItemModel(itemModel);
        itemStockDoMapper.insertSelective(itemStockDo);

        // 5. 返回创建完成的对象，通过getItemById获取
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDo> itemDoList = itemDoMapper.listItem();
        // 遍历itemDolist ,将每一个itemDo转化为itemModel
        List<ItemModel> itemModelList = itemDoList.stream().map(itemDo -> {
            // 加入itemStockDO，获取库存
            ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.getId());
            ItemModel itemModel = this.convertModelFromDataObject(itemDo, itemStockDo);
            return itemModel;
        }).collect(Collectors.toList());


        return itemModelList;
    }

    /**
     * @Description: // 通过Id查询商品详情
     **/
    @Override
    public ItemModel getItemById(Integer id) {
        ItemDo itemDo = itemDoMapper.selectByPrimaryKey(id);
        if (itemDo == null) {
            return null;
        }
        // 再根据item_id 查询出stock
        ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.getId());
        // 转化为Model类型
        ItemModel itemModel = convertModelFromDataObject(itemDo, itemStockDo);

        // 获取秒杀活动商品信息
        PromoModel promoModel = promoService.getPromoByItemId(id);
        // 判断：如果存在秒杀活动，且秒杀活动状态不等于3
        if (promoModel != null && promoModel.getStatus().intValue() != 3) {
            //将秒杀对象聚合进ItemModel，将该商品和秒杀对象关联起来
            itemModel.setPromoModel(promoModel);
        }

        return itemModel;
    }

    /**
     * @Description: // 下单时减库存
     **/
    @Override
    public Boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
        /*
            item商品表大部分用户查询，查询对应的商品信息
            库存表，在某些高压力的情况下做降级
            比如在微服务下，库存服务可以拆为item的展示服务（item表）和item的库存服务（item_stock表）
            这个item的库存服务独立出来，专门进行库存减操作。
            目前只操作item_stock表，为保证冻结操作的原子性，对item_stock表加锁，针对某一条记录进行加行锁，减掉对应的库存
            看减完后是否还大于表中库存。

            修改itemStockDoMapper映射文件，修改sql语句
         */
        //返回影响的条目数
        //sql成功执行返回的影响条目数不一定为1，如果购买数量大于库存，超卖，sql语句也会执行，但返回的就是0
        int afterRow = itemStockDoMapper.decreaseStock(itemId, amount);
        if (afterRow > 0) {
            // 更新库存成功
            return true;
        } else {
            // 更新库存失败
            return false;
        }


    }


    /**
     * @Description: // 下单成功增加销量数据
     **/
    @Override
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {

        itemDoMapper.increaseSales(itemId, amount);

    }

    // 将ItemModel转化为ItemDo
    private ItemDo convertItemDoFromItemMoldel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemDo itemDo = new ItemDo();

        //UserModel中的price是BigDecimal类型而不用Double，Double在java内部传到前端，会有精度问题，不精确
        //有可能1.9，显示时是1.999999，为此在Service层，将price定为比较精确的BigDecimal类型
        //但是在拷贝到Dao层时，存入的是Double类型，拷贝方法对应类型不匹配的属性，不会进行拷贝。
        //在拷贝完，将BigDecimal转为Double，再set进去
        BeanUtils.copyProperties(itemModel, itemDo);
        itemDo.setPrice(itemModel.getPrice().doubleValue());
        return itemDo;
    }

    // 将ItemModel转化为ItemStockDo
    private ItemStockDo convertItemStockDoFormItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemStockDo itemStockDo = new ItemStockDo();
        itemStockDo.setItemId(itemModel.getId());
        itemStockDo.setStock(itemModel.getStock());
        return itemStockDo;
    }


    // 将dataObject转化为model领域模型
    private ItemModel convertModelFromDataObject(ItemDo itemDo, ItemStockDo itemStockDo) {
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDo, itemModel);
        itemModel.setPrice(new BigDecimal(itemDo.getPrice()));
        itemModel.setStock(itemStockDo.getStock());
        return itemModel;
    }
}

