package com.wanmi.sbc.goods.api.request.cyclebuy;

import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>周期购活动上下架请求参数</p>
 * @author weiwenhao
 * @date 2021-01-21 09:15:37
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CycleBuySaleRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 周期购Id
	 */
	@ApiModelProperty(value = "周期购Id")
	@NotNull
	private Long id;


	/**
	 * 周期购上下架标识（0:下架,1:上架）
	 */
	@ApiModelProperty(value = "周期购上下架标识（0:下架,1:上架）")
	private AddedFlag addedFlag;
}