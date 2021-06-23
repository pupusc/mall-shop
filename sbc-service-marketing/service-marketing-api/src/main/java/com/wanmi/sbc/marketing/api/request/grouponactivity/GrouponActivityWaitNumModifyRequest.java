package com.wanmi.sbc.marketing.api.request.grouponactivity;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>拼团活动带拼团人数修改参数bean</p>
 * Created by of628-wenzhi on 2019-05-27-19:07.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class GrouponActivityWaitNumModifyRequest extends BaseRequest {

    private static final long serialVersionUID = -4413001500234038893L;

    /**
     * 活动ID
     */
    @ApiModelProperty(value = "拼团活动id")
    @NotNull
    private String grouponActivityId;

    /**
     * 增量数，若做减量，则传负值
     */
    @ApiModelProperty(value = "增量数，若做减量，则传负值")
    @NotNull
    private Integer num;
}
