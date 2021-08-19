package com.wanmi.sbc.account.api.response.finance.record;

import com.wanmi.sbc.account.bean.vo.SettlementVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 结算新增响应请求
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class SettlementAddResponse extends SettlementVO implements Serializable {

    private static final long serialVersionUID = -5120647741604865530L;


    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
}
