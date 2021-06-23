package com.wanmi.sbc.order.bean.dto;

import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 交易设置请求实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TradeSettingDTO implements Serializable{

    @ApiModelProperty(value = "配置类型")
    private ConfigType configType;

    @ApiModelProperty(value = "配置键")
    private ConfigKey configKey;

    /**
     * 开关状态
     */
    @ApiModelProperty(value = "开关状态")
    private Integer status;

    /**
     * 设置天数
     */
    @ApiModelProperty(value = "设置天数")
    private String context;
}
