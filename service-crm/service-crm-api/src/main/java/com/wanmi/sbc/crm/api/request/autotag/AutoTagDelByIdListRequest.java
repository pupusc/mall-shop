package com.wanmi.sbc.crm.api.request.autotag;

import com.wanmi.sbc.crm.api.request.CrmBaseRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

/**
 * <p>批量删除自动标签请求参数</p>
 * @author dyt
 * @date 2020-03-11 14:47:32
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoTagDelByIdListRequest extends CrmBaseRequest {
private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-idList
	 */
	@ApiModelProperty(value = "批量删除-idList")
	@NotEmpty
	private List<Long> idList;
}
