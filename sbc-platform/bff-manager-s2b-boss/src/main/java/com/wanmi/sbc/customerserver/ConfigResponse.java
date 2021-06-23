package com.wanmi.sbc.customerserver;

import com.wanmi.sbc.setting.bean.vo.OnlineServiceVO;
import com.wanmi.sbc.setting.bean.vo.SystemConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2019-12-30 16:10
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigResponse implements Serializable {
    @ApiModelProperty(value = "QQ客服")
    private OnlineServiceVO onlineServiceVO;

    @ApiModelProperty(value = "阿里云客服")
    private SystemConfigVO systemConfigVO;
}