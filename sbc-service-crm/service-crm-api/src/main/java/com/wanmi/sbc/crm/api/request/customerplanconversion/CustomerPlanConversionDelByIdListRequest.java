package com.wanmi.sbc.crm.api.request.customerplanconversion;

import com.wanmi.sbc.crm.api.request.CrmBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除运营计划转化效果请求参数</p>
 * @author zhangwenchang
 * @date 2020-02-12 00:16:50
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPlanConversionDelByIdListRequest extends CrmBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-主键IDList
	 */
	@ApiModelProperty(value = "批量删除-主键IDList")
	@NotEmpty
	private List<Long> idList;
}