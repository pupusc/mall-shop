package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
@ApiModel
/**
 * 订单代发货自动收货配置
 */
@Data
public class OrderAutoReceiveConfigGetResponse extends ConfigVO {
    private static final long serialVersionUID = -2499236478991437662L;
}
