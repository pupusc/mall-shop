package com.wanmi.sbc.order.api.request.purchase;

import com.wanmi.sbc.common.base.DistributeChannel;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-30
 */
@Data
@ApiModel
public class PurchaseClearLoseGoodsRequest implements Serializable {

    private static final long serialVersionUID = 6319314983286044072L;

    @ApiModelProperty(value = "客户id")
    @NotBlank
    private String userId;


    /**
     * 分销渠道信息-会员id
     */
    @ApiModelProperty(value = "分销渠道信息")
    DistributeChannel distributeChannel;

    @ApiModelProperty(value = "用户对象")
    CustomerVO customerVO;
}
