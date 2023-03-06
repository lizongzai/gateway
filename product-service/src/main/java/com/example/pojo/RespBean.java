package com.example.pojo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 功能描述: 公共返回对象结果
 *
 * @author lizongzai
 * @date 2023/03/06 14:45
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "RespBean对象", description = "")
public class RespBean {

  private long code;
  private String message;
  private Object obj;

  /**
   * @param message
   * @return
   * @Description //TODO 成功返回结果
   * @Author lizongzai
   */
  public static RespBean success(String message) {
    return new RespBean(200, message, null);
  }

  /**
   * @param message
   * @param obj
   * @return
   * @Description //TODO 成功返回结果
   * @Author lizongzai
   */
  public static RespBean success(String message, Object obj) {
    return new RespBean(200, message, obj);
  }

  /**
   * @param message
   * @return
   * @Description //TODO 失败返回结果
   * @Author lizongzai
   */
  public static RespBean error(String message) {
    return new RespBean(500, message, null);
  }

  /**
   * @param message
   * @param obj
   * @return
   * @Description //TODO 失败返回结果
   * @Author lizongzai
   */
  public static RespBean error(String message, Object obj) {
    return new RespBean(500, message, obj);
  }
}

