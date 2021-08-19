package com.wanmi.sbc.account.api.request.finance.record;

import com.wanmi.sbc.account.bean.enums.SettleStatus;
import com.wanmi.sbc.common.enums.StoreType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 结算分页查询请求
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class SettlementPageRequest extends BasePageRequest {

    private static final long serialVersionUID = -5115285012717445946L;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String endTime;

    /**
     * 店铺Id
     */
    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

    /**
     * 结算状态 {@link SettleStatus}
     */
    @ApiModelProperty(value = "结算状态")
    private SettleStatus settleStatus;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 批量店铺ID
     */
    @ApiModelProperty(value = "批量店铺ID")
    private List<Long> storeListId;

    /**
     * 商家类型 0供应商 1商家
     */
    @ApiModelProperty(value = "商家类型")
    private StoreType storeType;
}
