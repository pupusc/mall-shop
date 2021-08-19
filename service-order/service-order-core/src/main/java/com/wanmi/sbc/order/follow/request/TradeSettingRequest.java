package com.wanmi.sbc.order.follow.request;

import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 交易设置请求实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeSettingRequest implements Serializable{

    private ConfigType configType;

    private ConfigKey configKey;

    /**
     * 开关状态
     */
    private Integer status;

    /**
     * 设置天数
     */
    private String context;
}
