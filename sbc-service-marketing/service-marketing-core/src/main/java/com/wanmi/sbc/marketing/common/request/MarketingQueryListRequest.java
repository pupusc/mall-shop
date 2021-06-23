package com.wanmi.sbc.marketing.common.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.MarketingStatus;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 后台-促销活动列表
 */
@Data
public class MarketingQueryListRequest extends BaseQueryRequest {

    /**
     * 活动名称
     */
    private String marketingName;

    /**
     * 活动类型
     */
    private MarketingSubType marketingSubType;

    /**
     * 开始时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 目标客户
     */
    private Long targetLevelId;

    /**
     * 查询类型，0：全部，1：进行中，2：暂停中，3：未开始，4：已结束
     */
    private MarketingStatus queryTab;

    /**
     * 删除标记  0：正常，1：删除
     */
    public DeleteFlag delFlag;

    /**
     * 促销类型 0：满减 1:满折 2:满赠
     */
    private MarketingType marketingType;

    /**
     * 促销类型 0：满减 1:满折 2:满赠
     */
    @ApiModelProperty(value = "促销类型")
    private List<MarketingType> marketingTypeList;

    @ApiModelProperty(value = "多个店铺id")
    private List<Long> storeIds;

}
