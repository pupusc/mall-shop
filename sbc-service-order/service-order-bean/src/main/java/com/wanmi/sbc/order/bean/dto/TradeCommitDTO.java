package com.wanmi.sbc.order.bean.dto;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>客户端提交订单参数结构，包含除商品信息外的其他必要参数</p>
 * Created by of628-wenzhi on 2017-07-18-下午3:40.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class TradeCommitDTO extends BaseRequest {

    private static final long serialVersionUID = -1555919128448507297L;

    /**
     * 订单收货地址id，必传
     */
    @ApiModelProperty(value = "订单收货地址id", required = true)
    @NotBlank
    private String consigneeId;

    /**
     * 收货地址详细信息(包含省市区)，必传
     */
    @ApiModelProperty(value = "收货地址详细信息,包含省市区", required = true)
    @NotBlank
    private String consigneeAddress;

    /**
     * 收货地址修改时间，可空
     */
    @ApiModelProperty(value = "收货地址修改时间")
    private String consigneeUpdateTime;

    @Valid
    @NotEmpty
    @NotNull
    @ApiModelProperty(value = "按店铺拆分订单提交信息", required = true)
    private List<StoreCommitInfoDTO> storeCommitInfoList;

    /**
     * 选择的平台优惠券(通用券)id
     */
    @ApiModelProperty(value = "选择的平台优惠券(通用券)id")
    private String commonCodeId;

    /**
     * 是否强制提交，用于营销活动有效性校验，true: 无效依然提交， false: 无效做异常返回
     */
    @ApiModelProperty(value = "是否强制提交，用于营销活动有效性校验", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    public boolean forceCommit;

    /**
     * 是否拼团订单
     */
    @ApiModelProperty(value = "是否拼团订单")
    private boolean grouponFlag;

    /**
     * 订单拼团信息
     */
    @ApiModelProperty(value = "订单拼团信息")
    private TradeGrouponDTO tradeGroupon;
}
