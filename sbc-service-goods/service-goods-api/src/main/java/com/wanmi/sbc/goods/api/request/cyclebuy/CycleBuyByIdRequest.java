package com.wanmi.sbc.goods.api.request.cyclebuy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>单个查询周期购活动请求参数</p>
 * @author weiwenhao
 * @date 2021-01-21 09:15:37
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CycleBuyByIdRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 周期购Id
	 */
	@ApiModelProperty(value = "周期购Id")
	@NotNull
	private Long id;
}