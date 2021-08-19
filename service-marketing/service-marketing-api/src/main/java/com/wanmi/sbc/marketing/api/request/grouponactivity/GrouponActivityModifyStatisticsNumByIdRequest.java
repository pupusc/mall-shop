package com.wanmi.sbc.marketing.api.request.grouponactivity;

import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 根据不同拼团状态更新不同的统计数据（已成团、待成团、团失败人数）
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrouponActivityModifyStatisticsNumByIdRequest implements Serializable {

    private static final long serialVersionUID = -6081625447225022375L;

    /**
     * 拼团活动ID
     */
    @ApiModelProperty(value = "拼团活动ID")
    @NotBlank
    private String grouponActivityId;

    /**
     * 人数
     */
    @ApiModelProperty(value = "人数")
    @NonNull
    private Integer grouponNum;

    /**
     * 拼团状态
     */
    @ApiModelProperty(value = "拼团状态")
    @NonNull
    private GrouponOrderStatus grouponOrderStatus;


}
