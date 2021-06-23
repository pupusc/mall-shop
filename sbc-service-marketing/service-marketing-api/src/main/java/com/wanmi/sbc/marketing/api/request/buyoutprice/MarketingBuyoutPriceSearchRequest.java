package com.wanmi.sbc.marketing.api.request.buyoutprice;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.dto.MarketingQueryBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: weiwenhao
 * @Description:
 * @Date: 2020-04-14
 */
@ApiModel
@Data
public class MarketingBuyoutPriceSearchRequest extends MarketingQueryBaseDTO {
    private static final long serialVersionUID = 4709873515000508764L;

    /**
     * 营销名称
     */
    @ApiModelProperty(value = "营销名称")
    private String marketingName;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;
    /**
     * 搜索条件结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTimeBegin;

    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 查询平台类型
     */
    @ApiModelProperty(value = "查询平台类型")
    private Platform platform;

}
