package com.xiaao.miaosha.dao;

import com.xiaao.miaosha.dataobject.PromoDo;

public interface PromoDoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Sun Feb 17 21:09:03 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Sun Feb 17 21:09:03 CST 2019
     */
    int insert(PromoDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Sun Feb 17 21:09:03 CST 2019
     */
    int insertSelective(PromoDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Sun Feb 17 21:09:03 CST 2019
     */
    PromoDo selectByPrimaryKey(Integer id);

    // 通过itemId查找秒杀活动
    PromoDo selectByItemId(Integer itemId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Sun Feb 17 21:09:03 CST 2019
     */
    int updateByPrimaryKeySelective(PromoDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Sun Feb 17 21:09:03 CST 2019
     */
    int updateByPrimaryKey(PromoDo record);
}