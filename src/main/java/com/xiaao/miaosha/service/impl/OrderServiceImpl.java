package com.xiaao.miaosha.service.impl;
/**
 * @author: Xia-ao
 * @create: 2019-02-14 18:03
 **/

import com.sun.org.apache.xpath.internal.operations.Or;
import com.xiaao.miaosha.dao.OrderDoMapper;
import com.xiaao.miaosha.dao.SequenceDoMapper;
import com.xiaao.miaosha.dataobject.OrderDo;
import com.xiaao.miaosha.dataobject.SequenceDo;
import com.xiaao.miaosha.error.BusinessException;
import com.xiaao.miaosha.error.EmBusinessErr;
import com.xiaao.miaosha.service.ItemService;
import com.xiaao.miaosha.service.OrderService;
import com.xiaao.miaosha.service.UserService;
import com.xiaao.miaosha.service.model.ItemModel;
import com.xiaao.miaosha.service.model.OrderModel;
import com.xiaao.miaosha.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @className: OrderServiceImpl
 * @description: 交易订单Impl
 * @author: Xia-ao
 * @create: 2019-02-14 18:03
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDoMapper orderDoMapper;

    @Autowired
    private SequenceDoMapper sequenceDoMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException {

        // 1. 校验下单状态，商品是否存在，数量是否正确，用户是否合法
        // 判断商品是否存在
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null) {
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDATION_ERROR, "商品信息不存在");
        }

        // 判断用户是否合法
        UserModel userModel = userService.getUserById(userId);
        if (userModel == null) {
            throw new BusinessException(EmBusinessErr.USER_NOT_EXIST, "用户信息不存在");
        }

        // 判断数量是否合理
        if (amount < 0 || amount > 99) {
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDATION_ERROR, "商品数量不合理");
        }

        // 插入秒杀活动校验
        // 如果promoId不等于null
        if (promoId != null) {
            // 1) 校验这个活动是在使用这个商品
            //看传过来的秒杀模型id是否和商品模型中聚合的秒杀模型的id一致（该商品有秒杀活动，会将秒杀模型聚合进商品Model）
            if (promoId.intValue() != itemModel.getPromoModel().getId()) {
                throw new BusinessException(EmBusinessErr.PARAMETER_VALIDATION_ERROR, "活动信息不正确！");

                // 2) 如果是该商品的秒杀活动，还需要校验是不是正在进行的秒杀活动，只有正在进行的秒杀活动才能以秒杀价格落单
            } else if (itemModel.getPromoModel().getStatus() != 2) {
                throw new BusinessException(EmBusinessErr.PARAMETER_VALIDATION_ERROR, "秒杀活动不再在进行中");
            }
        }


        // 2. 落单减库存 VS 支付减库存，这里选择落单减库存
        boolean result = itemService.decreaseStock(itemId, amount);
        if (!result) {
            // 扣减库存失败
            throw new BusinessException(EmBusinessErr.STOCK_NOT_ENOUGH);
        }
        // 否则 扣减库存成功

        // 3. 订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        // 如果存在秒杀，则下单价格是秒杀价格
        if (promoId != null) {
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        } else {
            // 否则就是平销价格
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));

        //给订单模型设置秒杀活动id
        orderModel.setPromoId(promoId);

        // 手动生成交易流水单号，并插入数据库
        orderModel.setId(generateOrderNum());
        OrderDo orderDo = convertFromOrderModel(orderModel);
        orderDoMapper.insertSelective(orderDo);

        // 同时将商品销量增加
        itemService.increaseSales(itemId, amount);

        // 4. 返回orderModel


        return orderModel;
    }


    /**
     * @Description: // 生成16位格式的交易单号
     * <p>
     * <p>
     * 为了保证订单号的全剧唯一，在生成方法中加注解，并设置传播机制是开新_NEW
     * 这样即使外层创建订单操作失败要回滚，里面的生成订单方法都会生成一个新的事务进行提交。
     * 这样下一个事务操作订单生成，会拿到最新的，而不是回滚时生成过的
     **/
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrderNum() {

        StringBuffer sb = new StringBuffer();

        // 16位订单编号
        // 前8位位时间信息，年月日， 归档记录切分点
        LocalDateTime nowTime = LocalDateTime.now();
        //格式化后的格式是2018-12-12带横线的，将-去掉
        String nowDate = nowTime.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        sb.append(nowDate);


        // 中间6位为自增序列，每一天某一个时间节点，保证订单号不重复，
        //数据库中创建一张自增序列表sequence_info  从sequence_info表中获取当前序列值
        int sequence = 0;
        SequenceDo sequenceDo = sequenceDoMapper.getSequenceByName("order_info");
        // 获取库中当前序列值
        sequence = sequenceDo.getCurrentValue();
        // 获取之后生成新的序列值，步长+1，马上更新表中的sequence
        sequenceDo.setCurrentValue(sequenceDo.getCurrentValue() + sequenceDo.getStep());
        sequenceDoMapper.updateByPrimaryKeySelective(sequenceDo);

        // 然后拼足6位自增序列
        String sequenceStr = String.valueOf(sequence);
        for (int i = 0; i < 6 - sequenceStr.length(); i++) {
            sb.append(0);
        }
        sb.append(sequenceStr);     // 将序列拼接上去

        //最后2位为分库分表位，00-99，分库分表，订单水平拆分
        //订单信息落到拆分后的100个库的100张表中，分散数据库从查询和落单压力
        //订单号不变，这条订单记录一定会落到某一个库的某一张表上
//        Integer userId = 1000122;
//        userId % 100
        //暂时写死
        sb.append("00");

        return sb.toString();

    }

    // OrderModel -> OrderDo
    private OrderDo convertFromOrderModel(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        }
        OrderDo orderDo = new OrderDo();
        BeanUtils.copyProperties(orderModel, orderDo);
        orderDo.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDo.setOrderPrice(orderModel.getOrderPrice().doubleValue());

        return orderDo;
    }
}
