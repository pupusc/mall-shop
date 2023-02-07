package com.wanmi.sbc.goods.api.request.SuspensionV2;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;


/**
 * <p>悬浮框信息查询</p>
 * @author lws
 * @date 2023-02-4
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuspensionByTypeRequest extends BaseRequest {
    private static final long serialVersionUID = 1L;


    /**
     * type
     */
    @ApiModelProperty(value = "type")
    @NotNull
    private Long type;


}
