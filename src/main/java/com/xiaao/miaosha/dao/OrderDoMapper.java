package com.xiaao.miaosha.dao;

import com.xiaao.miaosha.dataobject.OrderDo;

public interface OrderDoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Thu Feb 14 12:55:02 CST 2019
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Thu Feb 14 12:55:02 CST 2019
     */
    int insert(OrderDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Thu Feb 14 12:55:02 CST 2019
     */
    int insertSelective(OrderDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Thu Feb 14 12:55:02 CST 2019
     */
    OrderDo selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Thu Feb 14 12:55:02 CST 2019
     */
    int updateByPrimaryKeySelective(OrderDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Thu Feb 14 12:55:02 CST 2019
     */
    int updateByPrimaryKey(OrderDo record);
}