package com.sky.controller.user;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    //添加购物车
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加购物车，新增信息为：{}",shoppingCartDTO);
        shoppingCartService.save(shoppingCartDTO);
        return Result.success();
    }

    //查看购物车
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        log.info("查看购物车");
        List<ShoppingCart> list = shoppingCartService.showShoppingCart();
        return Result.success(list);
    }

    //清空购物车
    @DeleteMapping("/delete")
    public Result delete(){
        log.info("清空购物车");
        shoppingCartService.deleteByUserId();
        return Result.success();
    }

    //删除或减少单个商品
    @PostMapping("/sub")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("减少购物车中单个商品的数量: {}",shoppingCartDTO);
        shoppingCartService.subItem(shoppingCartDTO);
        return Result.success();
    }

}
