package com.sky.mapper;


import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    //查询购物车
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    //增加单品数量
    @Update("update shopping_cart set number = #{number} + 1 where id = #{id}")
    void updateNumber(ShoppingCart shoppingCart);

    //添加一条购物车数据
    @Insert("insert into shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time)" +
            "values (#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    //清空购物车
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);

    //删除单条菜品类型的购物车数据
    @Delete("delete from shopping_cart where dish_id = #{dishId} and user_id = #{userId}")
    void deleteByDishId(ShoppingCart cart);

    //减少单品数量
    @Update("update shopping_cart set number = #{number} - 1 where id = #{id}")
    void subNumber(ShoppingCart cart);

    //删除单条套餐类型的购物车数据
    @Delete("delete from shopping_cart where setmeal_id = #{setmealId} and user_id = #{userId}")
    void deleteBySetmealId(ShoppingCart cart);



}
