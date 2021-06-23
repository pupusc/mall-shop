package com.wanmi.sbc.setting.api.request;

import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 系统配置更新请求
 * @author Created by liutao on 2019/2/22.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ConfigListModifyRequest extends SettingBaseRequest {

    private static final long serialVersionUID = 7726275470517935663L;

    @ApiModelProperty(value = "系统配置更新请求信息")
    List<ConfigVO> configRequestList;
}
