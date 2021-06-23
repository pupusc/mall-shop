package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 订单设置
 */
@ApiModel
@Data
public class TradeConfigGetByTypeResponse extends ConfigVO {
    private static final long serialVersionUID = 6195899434449467299L;
}
