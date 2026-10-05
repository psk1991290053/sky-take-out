package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    ShoppingCartMapper shoppingCartMapper;
    @Autowired
    DishMapper dishMapper;
    @Autowired
    SetmealMapper setmealMapper;

    //添加进购物车
    public void save(ShoppingCartDTO shoppingCartDTO) {

        //先查看数据库中，该用户是否添加过相同的菜品进入购物车
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> carts = shoppingCartMapper.list(shoppingCart);

        //若添加过，则让数据库中的number字段+1
        if(carts != null && carts.size() > 0){
            ShoppingCart cart = carts.get(0);
            shoppingCartMapper.updateNumber(cart);
        }
        //若没有添加过，则在数据库中插入一条新的数据
        else{
            //判断插入的是菜品还是套餐
            Long dishId = shoppingCart.getDishId();
            if(dishId != null){
                //确定新添加的是菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setImage(dish.getImage());
            }else{
                //确定新添加的是套餐
                Long setmealId = shoppingCart.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(setmeal.getImage());
            }
            //设置公共属性
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            //插入
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    //查看购物车
    public List<ShoppingCart> showShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }

    //清空购物车
    public void deleteByUserId() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }

    //减少购物车中的单个商品
    public void subItem(ShoppingCartDTO shoppingCartDTO) {
        //创建shoppingCart对象
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(userId);
        ShoppingCart cart = shoppingCartMapper.list(shoppingCart).get(0);//获得了要修改的那条数据
        //判断要减少的是菜品类型还是套餐类型
        Long dishId = shoppingCart.getDishId();
        if(dishId != null){
            //确定要减少的是菜品类型
            //若该购物车中只有一个此类菜品 则删除该条数据
            if(cart.getNumber() == 1){
                shoppingCartMapper.deleteByDishId(cart);
            }else{
                //若该购物车中，此类菜品有多个，则减少一个
                shoppingCartMapper.subNumber(cart);
            }
        }else{
            //确定要减少的是套餐类型
            Long setmealId = shoppingCart.getSetmealId();
            //若该购物车中只有一个此类菜品 则删除该条数据
            if(cart.getNumber() == 1){
                shoppingCartMapper.deleteBySetmealId(cart);
            }else{
                //若该购物车中，此类菜品有多个，则减少一个
                shoppingCartMapper.subNumber(cart);
            }
        }
    }

}
