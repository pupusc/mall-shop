package com.wanmi.sbc.marketing.api.request.buyoutprice;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: weiwenhao
 * @Description:
 * @Date: 2020-04-14
 */
@ApiModel
@Data
public class MarketingBuyoutPriceIdRequest implements Serializable {
    private static final long serialVersionUID = 5922638409956076645L;
    /**
     * 营销ID
     */
    @ApiModelProperty(value = "营销Id")
    @NotNull
    private Long marketingId;

}
