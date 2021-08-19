package com.wanmi.sbc.goods.api.request.groupongoodsinfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * <p>根据活动ID批量更新是否精选</p>
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponGoodsInfoModifyStickyRequest implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * 拼团活动ID集合
	 */
	@ApiModelProperty(value = "拼团活动ID集合")
	@NonNull
	private List<String> grouponActivityIds;

	/**
	 * 是否精选
	 */
	@ApiModelProperty(value = " 是否精选")
	@NonNull
	private Boolean sticky;
}