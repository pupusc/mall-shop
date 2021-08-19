package com.wanmi.sbc.order.api.request.pointstrade;

import com.wanmi.sbc.common.base.Operator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName PointsTradeRemedySellerRemarkRequest
 * @Description 修改卖家备注Request
 * @Author lvzhenwei
 * @Date 2019/5/22 14:28
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class PointsTradeRemedySellerRemarkRequest implements Serializable {

    private static final long serialVersionUID = -5594735750530343734L;

    /**
     * 交易id
     */
    @ApiModelProperty(value = "交易id")
    private String tid;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String sellerRemark;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Operator operator;
}
