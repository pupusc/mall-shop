package com.wanmi.sbc.marketing.api.request.marketingsuits;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.dto.GoodsSuitsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingSuitsSaveRequest extends BaseRequest {

    /**
     * 活动id
     */
    @ApiModelProperty(value = "活动id")
    private Long marketingId;


    /**
     * 促销名称
     */
    @ApiModelProperty(value = "促销名称")
    @NotNull
    private String suitsName;

    /**
     *活动开始时间
     */
    @ApiModelProperty(value = "活动开始时间")
    @NotNull
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime beginTime;

    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @NotNull
    private LocalDateTime endTime;

    /**
     * 套餐主图
     */
    @NotNull
    @ApiModelProperty(value = "套餐主图")
    private String suitsPictureUrl;

    /**
     * 数量是否全部相同标识
     */
    @ApiModelProperty(value = "数量是否全部相同标识")
    @NotNull
    private DefaultFlag goodsSkuNumIdentify;

    /**
     * 添加组合商品集合
     */
    @ApiModelProperty(value = "添加组合商品集合")
    private List<GoodsSuitsDTO> goodsSuitsDTOList;

    /**
     * 参加会员 0:全部等级  other:其他等级
     */
    @ApiModelProperty(value = "参加会员", dataType = "com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel")
    @NotBlank
    private String joinLevel;

}
