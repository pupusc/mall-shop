package com.wanmi.sbc.goods.api.request.groupongoodsinfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponGoodsByGoodsInfoIdAndTimeRequest implements Serializable {
    private static final long serialVersionUID = 5794652344512329593L;

    /**
     * 拼团活动ID
     */
    @ApiModelProperty(value = "拼团活动ID")
    @NotEmpty
    private List<String> goodsInfoIds;

    /**
     * 拼团活动开始时间
     */
    @ApiModelProperty(value = "拼团活动开始时间")
    @NotNull
    private LocalDateTime startTime;

    /**
     * 拼团活动结束时间
     */
    @ApiModelProperty(value = "拼团活动结束时间")
    @NotNull
    private LocalDateTime endTime;
}