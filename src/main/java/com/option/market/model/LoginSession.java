package com.option.market.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: this 登录会话
 * @Date: Create in 10:21 2020/2/22
 * @Modified by:
 */
@Data
@ToString
public class LoginSession implements Serializable {

	private static final long serialVersionUID = 610401627875700703L;
	private Integer userId;
	private Map<String,Object> extraValue=new HashMap<>();

}
