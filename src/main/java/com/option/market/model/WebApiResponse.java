package com.option.market.model;

import lombok.Data;
import lombok.ToString;

/**
 * @Description: this 全局信息返回实体类格式
 * @Date: Create in 10:21 2020/2/22
 * @Modified by:
 */
@Data
@ToString
public class WebApiResponse {
    private String code;
    private String msg;
    private Object data;

}
