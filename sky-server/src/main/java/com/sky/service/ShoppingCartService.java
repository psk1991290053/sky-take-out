package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    //添加购物车
    public void save(ShoppingCartDTO shoppingCartDTO);

    //查看购物车
    List<ShoppingCart> showShoppingCart();

    //清空购物车
    void deleteByUserId();

    //减少购物车中的单个商品
    void subItem(ShoppingCartDTO shoppingCartDTO);
}
