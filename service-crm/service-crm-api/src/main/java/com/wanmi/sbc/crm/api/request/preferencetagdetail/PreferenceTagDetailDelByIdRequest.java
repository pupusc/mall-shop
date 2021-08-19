package com.wanmi.sbc.crm.api.request.preferencetagdetail;

import com.wanmi.sbc.crm.api.request.CrmBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除偏好标签明细请求参数</p>
 * @author dyt
 * @date 2020-03-11 14:58:07
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreferenceTagDetailDelByIdRequest extends CrmBaseRequest {
private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @NotNull
    private Long id;
}
