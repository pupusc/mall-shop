package com.wanmi.sbc.setting.api.request.erplogisticsmapping;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个查询erp系统物流编码映射请求参数</p>
 * @author weiwenhao
 * @date 2021-04-03 13:38:51
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErpLogisticsMappingByErpLogisticsCodeRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * erp系统物流编码
	 */
	@ApiModelProperty(value = "erp系统物流编码")
	private String erpLogisticsCode;

	/**
	 * wm系统物流编码
	 */
	@ApiModelProperty(value = "wm系统物流编码")
	private String wmLogisticsCode;
}