package com.wanmi.sbc.crm.api.request.tagdimension;

import com.wanmi.sbc.crm.api.request.CrmBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除标签维度请求参数</p>
 * @author dyt
 * @date 2020-03-12 16:00:30
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDimensionDelByIdRequest extends CrmBaseRequest {
private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @NotNull
    private Long id;
}
