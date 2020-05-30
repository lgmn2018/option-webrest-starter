package com.option.market.constant;

/**
 * @Description 通用（服务器异常，参数校验异常，操作异常）
 */
public interface CommonCodeConst {
    // 传入字段校验错误
    String FIELD_ERROR = "100100";

    // 接口请求失败，内部异常
    String SERVICE_ERROR = "100103";

    String INVALID_REQUEST = "100104";

    String SERIVCE_SUCCESS = "100200";

    String UPDATE_ERROR = "100105";

    String OPER_TOO_MANY = "100107";
    String DATA_NOT_EXISTED = "100108";
    String CHANNEL_CLOSE = "100109";
    String DATA_REPEAT = "100110";
}
