package com.wanmi.sbc.crm.api.request.tagparam;

import com.wanmi.sbc.crm.api.request.CrmBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除标签参数请求参数</p>
 * @author dyt
 * @date 2020-03-12 15:59:49
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagParamDelByIdRequest extends CrmBaseRequest {
private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @NotNull
    private Long id;
}
