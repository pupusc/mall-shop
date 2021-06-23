package com.wanmi.sbc.goods.api.response.enterprise;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 审核企业购商品时返回的标志
 *
 * @author CHENLI
 * @dateTime 2019/3/26 上午9:33
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseGoodsAuditResponse implements Serializable {

    private static final long serialVersionUID = 6523387983737024542L;

    /**
     * 成功标志
     */
    @ApiModelProperty(value = "成功标志")
    private String backErrorCode;
}
