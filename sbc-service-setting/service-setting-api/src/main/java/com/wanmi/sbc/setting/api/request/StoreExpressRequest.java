package com.wanmi.sbc.setting.api.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 店铺快递公司前端请求实体类
 * Author: bail
 * Time: 2017/11/20.10:22
 */
@ApiModel
@Data
public class StoreExpressRequest extends BaseQueryRequest {
    private static final long serialVersionUID = -6984783218659566242L;
    //物流公司标识
    @ApiModelProperty(value = "物流公司标识")
    private Long expressCompanyId;
}
