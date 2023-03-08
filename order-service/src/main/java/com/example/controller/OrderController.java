package com.example.controller;


import com.example.pojo.Order;
import com.example.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lizongzai
 * @date 2023/03/06 14:45
 * @since 1.0.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {

  @Autowired
  private IOrderService orderService;


  /**
   * 根据主键查询订单-->调用商品服务/product/list
   *
   * @param id
   * @return
   */
  @GetMapping("/{id}/product/list")
  public Order selectOrderById(@PathVariable("id") Integer id) {
    return orderService.selectOrderById(id);
  }

  /**
   * 根据主键查询订单-->调用商品服务/product/listByIds
   *
   * @param id
   * @return
   */
  @GetMapping("/{id}/product/listByIds")
  public Order queryOrderById(@PathVariable("id") Integer id) {
    return orderService.queryOrderById(id);
  }

  /**
   * 根据主键查询订单-->调用商品服务/product/{id}
   *
   * @param id
   * @return
   */
  @GetMapping("/{id}/product")
  public Order searchOrderById(@PathVariable("id") Integer id) {
    return orderService.searchOrderById(id);
  }

  @GetMapping("/index")
  public String index() {
    return "Hello world";
  }

}
