package com.wanmi.sbc.crm.api.request.autotag;

import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * <p>单个查询自动标签请求参数</p>
 * @author dyt
 * @date 2020-03-11 14:47:32
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoTagByIdRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	@NotNull
	private Long id;

}