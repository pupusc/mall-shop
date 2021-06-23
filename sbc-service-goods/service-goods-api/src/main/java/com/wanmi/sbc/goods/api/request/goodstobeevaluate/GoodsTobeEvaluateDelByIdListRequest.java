package com.wanmi.sbc.goods.api.request.goodstobeevaluate;

import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除订单商品待评价请求参数</p>
 * @author lzw
 * @date 2019-03-20 14:47:38
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsTobeEvaluateDelByIdListRequest implements Serializable {

	private static final long serialVersionUID = -6213048272968409863L;

	/**
	 * 批量删除-idList
	 */
	@ApiModelProperty(value = "id")
	@NotEmpty
	private List<String> idList;
}