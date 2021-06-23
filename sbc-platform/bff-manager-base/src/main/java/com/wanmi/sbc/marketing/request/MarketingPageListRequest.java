package com.wanmi.sbc.marketing.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.MarketingStatus;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-30 14:03
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingPageListRequest extends BaseQueryRequest {

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String marketingName;

    /**
     * 活动类型
     */
    @ApiModelProperty(value = "活动类型")
    private MarketingSubType marketingSubType;

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
     * 目标客户
     */
    @ApiModelProperty(value = "目标客户")
    private Long targetLevelId;

    /**
     * 查询类型，0：全部，1：进行中，2：暂停中，3：未开始，4：已结束
     */
    @ApiModelProperty(value = "查询类型")
    private MarketingStatus queryTab;

    /**
     * 删除标记  0：正常，1：删除
     */
    @ApiModelProperty(value = "删除标记")
    public DeleteFlag delFlag;

    /**
     * 判断是否获取一口价的营销规则，true是获取，false是不获取
     */
    @ApiModelProperty(value = "判断是否展示营销规则")
    private Boolean rules;

    /**
     * 是否显示店铺名称
     */
    @ApiModelProperty(value = "是否显示店铺名称")
    private Boolean showStoreNameFlag;

    /**
     * 促销类型 0：满减 1:满折 2:满赠
     */
    @ApiModelProperty(value = "促销类型")
    private MarketingType marketingType;

    /**
     * 促销类型 0：满减 1:满折 2:满赠
     */
    @ApiModelProperty(value = "促销类型")
    private List<MarketingType> marketingTypeList;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 多个店铺名称
     */
    @ApiModelProperty(value = "多个店铺id")
    private List<Long> storeIds;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

}
