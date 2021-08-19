package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>根据拼团活动ID、SPU编号查询拼团价格最小的拼团SKU信息</p>
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponGoodsInfoByActivityIdAndGoodsIdDTO implements Serializable {


	/**
	 * 拼团活动ID
	 */
	@ApiModelProperty(value = "拼团活动ID")
	@NotBlank
	private String grouponActivityId;

	/**
	 * SPU编号
	 */
	@ApiModelProperty(value = "SPU编号")
	@NotBlank
	private String goodsId;
}