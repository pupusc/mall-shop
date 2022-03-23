package com.soybean.mall.order.request;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TradeItemConfirmRequest extends BaseRequest {

    private static final long serialVersionUID = -3106790833666168436L;

    /**
     * 商品信息，必传
     */
    @ApiModelProperty(value = "商品信息")
    @NotEmpty
    @Valid
    private List<TradeItemRequest> tradeItems;

    /**
     * 订单营销信息快照
     */
    @ApiModelProperty(value = "订单营销信息快照")
    private List<TradeMarketingDTO> tradeMarketingList;

    /**
     * 是否强制确认，用于营销活动有效性校验，true: 无效依然提交， false: 无效做异常返回
     */
    @ApiModelProperty(value = "是否强制确认，用于营销活动有效性校验,true: 无效依然提交， false: 无效做异常返回")
    public boolean forceConfirm;

    @ApiModelProperty("收货地址区的地址码")
    public String areaId;

}
