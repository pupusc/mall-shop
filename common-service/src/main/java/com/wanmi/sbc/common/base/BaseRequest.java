package com.wanmi.sbc.common.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;

/**
 * 请求基类
 * Created by aqlu on 15/11/30.
 */
@ApiModel
@Data
@Slf4j
public class BaseRequest implements Serializable {

    /**
     * 登录用户Id
     */
    private String userId;

    /**
     * 店铺ID
     * 由于前面有很多继承它的类已经定义了storeId，作用与storeId一样，继续它没有必要再起
     */
    private Long baseStoreId;

    /**
     * 获取源头 1 H5 2 小程序 3 主站 4 销售渠道
     */
    private Integer goodsChannelType;

    /**
     * 统一参数校验入口
     */
    public void checkParam(){
        log.info("统一参数校验入口");
    }

    /**
     * 统一参数校验敏感词入口
     */
    public String checkSensitiveWord(){return null;}
}
