package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApiModel
@Data
public class AuditConfigListResponse implements Serializable {
    private static final long serialVersionUID = -4327931678383243094L;
    /**
     * 审核开关列表
     */
    @ApiModelProperty(value = "审核开关列表")
    private List<ConfigVO> configVOList = new ArrayList<>();
}
