package com.wanmi.sbc.customer.api.request.enterpriseinfo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>企业信息表根据会员id批量查询请求参数</p>
 * @author TangLian
 * @date 2020-03-03 14:11:45
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseInfoListByCustomerIdsRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-会员IdList
	 */
	@NotNull
	@ApiModelProperty(value = "批量查询-会员IdList")
	private List<String> customerIdList;

}