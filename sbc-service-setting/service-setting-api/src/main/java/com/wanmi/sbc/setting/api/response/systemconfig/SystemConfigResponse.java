package com.wanmi.sbc.setting.api.response.systemconfig;

import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import com.wanmi.sbc.setting.bean.vo.SystemConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by feitingting on 2019/11/6.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfigResponse {

    @ApiModelProperty(value = "系统配置信息")
    /**
     * 系统配置信息
     */
    private List<ConfigVO> configVOList;

    private List<SystemConfigVO> systemConfigVOList;
}
