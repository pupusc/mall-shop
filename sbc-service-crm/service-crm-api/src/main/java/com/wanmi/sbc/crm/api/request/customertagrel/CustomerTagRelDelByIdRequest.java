package com.wanmi.sbc.crm.api.request.customertagrel;

import com.wanmi.sbc.crm.api.request.CrmBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除会员标签关联请求参数</p>
 * @author dyt
 * @date 2019-11-12 14:49:08
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTagRelDelByIdRequest extends CrmBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	@NotNull
	private Long id;
}