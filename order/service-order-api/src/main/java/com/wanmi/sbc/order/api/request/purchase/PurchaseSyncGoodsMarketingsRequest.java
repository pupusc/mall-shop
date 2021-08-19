package com.wanmi.sbc.order.api.request.purchase;

import com.wanmi.sbc.marketing.bean.vo.MarketingViewVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-30
 */
@Data
@ApiModel
public class PurchaseSyncGoodsMarketingsRequest implements Serializable {

    private static final long serialVersionUID = -9184329971487996835L;

    @ApiModelProperty(value = "营销信息")
    private Map<String, List<MarketingViewVO>> goodsMarketingMap;

    @ApiModelProperty(value = "客户id")
    private String customerId;
}
