package com.wanmi.sbc.setting.api.request.hovernavmobile;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;


/**
 * <p>单个查询移动端悬浮导航栏请求参数</p>
 *
 * @author dyt
 * @date 2020-04-29 14:28:21
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoverNavMobileByIdRequest extends BaseRequest {
    private static final long serialVersionUID = 1L;

    /**
     * storeId
     */
    @NotNull
    @ApiModelProperty(value = "storeId")
    private Long storeId;

}