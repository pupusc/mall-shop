package com.wanmi.sbc.setting.api.response.yunservice;

import com.wanmi.sbc.setting.bean.vo.SystemConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 云配置YunConfigByIdResponse
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YunConfigListResponse implements Serializable {

    private static final long serialVersionUID = 3445321139907523117L;

    @ApiModelProperty(value = "云服务配置信息")
    private List<SystemConfigVO> systemConfigVOS;
}
