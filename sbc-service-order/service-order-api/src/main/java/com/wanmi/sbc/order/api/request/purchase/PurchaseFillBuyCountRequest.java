package com.wanmi.sbc.order.api.request.purchase;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-05
 */
@Data
@ApiModel
public class PurchaseFillBuyCountRequest implements Serializable {

    private static final long serialVersionUID = 6936710044864171537L;

    @ApiModelProperty(value = "商品信息")
    @NotNull
    @Size(min = 1)
    private List<GoodsInfoVO> goodsInfoList;

    @ApiModelProperty(value = "客户id")
    @NotBlank
    private String customerId;

    /**
     * 邀请人id-会员id
     */
    @ApiModelProperty(value = "邀请人id")
    String inviteeId;
}
