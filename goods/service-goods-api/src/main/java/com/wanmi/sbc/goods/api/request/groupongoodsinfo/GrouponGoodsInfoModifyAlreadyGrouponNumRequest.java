package com.wanmi.sbc.goods.api.request.groupongoodsinfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>根据活动ID、SKU编号更新已成团人数</p>
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponGoodsInfoModifyAlreadyGrouponNumRequest implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * 拼团活动ID
	 */
	@ApiModelProperty(value = "拼团活动ID")
	@NotBlank
	private String grouponActivityId;

	/**
	 * 拼团SKU编号集合
	 */
	@ApiModelProperty(value = "拼团SKU编号集合")
	@NotBlank
	private List<String> goodsInfoIds;

	/**
	 * 已成团人数
	 */
	@ApiModelProperty(value = "已成团人数")
	@NotNull
	private Integer alreadyGrouponNum;
}