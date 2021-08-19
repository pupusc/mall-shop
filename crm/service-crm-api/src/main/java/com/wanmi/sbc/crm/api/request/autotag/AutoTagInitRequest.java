package com.wanmi.sbc.crm.api.request.autotag;

import com.wanmi.sbc.crm.api.request.CrmBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>批量初始化标签请求参数</p>
 * @author dyt
 * @date 2020-03-11 14:47:32
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoTagInitRequest extends CrmBaseRequest {
private static final long serialVersionUID = 1L;

	/**
	 * 批量选择-标签ids
	 */
	@ApiModelProperty(value = "批量选择-系统标签ids")
	@NotEmpty
	private List<Long> tagIds;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createPerson;
}
