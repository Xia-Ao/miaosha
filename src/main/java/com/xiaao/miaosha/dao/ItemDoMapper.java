package com.xiaao.miaosha.dao;

import com.xiaao.miaosha.dataobject.ItemDo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemDoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Sat Feb 09 22:38:13 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Sat Feb 09 22:38:13 CST 2019
     */
    int insert(ItemDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Sat Feb 09 22:38:13 CST 2019
     */
    int insertSelective(ItemDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Sat Feb 09 22:38:13 CST 2019
     */
    ItemDo selectByPrimaryKey(Integer id);


    // 按照商品描述排序
    List<ItemDo> listItem();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Sat Feb 09 22:38:13 CST 2019
     */
    int updateByPrimaryKeySelective(ItemDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Sat Feb 09 22:38:13 CST 2019
     */
    int updateByPrimaryKey(ItemDo record);

    int increaseSales(@Param("id") Integer itemId, @Param("amount") Integer amount);
}