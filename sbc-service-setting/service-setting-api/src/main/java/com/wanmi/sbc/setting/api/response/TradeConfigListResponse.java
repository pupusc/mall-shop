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
public class TradeConfigListResponse implements Serializable {
    private static final long serialVersionUID = 2208123519563377567L;
    /**
     * 订单设置列表
     */
    @ApiModelProperty(value = "订单设置列表")
    private List<ConfigVO> configVOList = new ArrayList<>();
}
