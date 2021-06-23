package com.wanmi.sbc.goods.api.request.storetobeevaluate;

import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除店铺服务待评价请求参数</p>
 * @author lzw
 * @date 2019-03-20 17:01:46
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreTobeEvaluateDelByIdListRequest implements Serializable {

	private static final long serialVersionUID = -7751127145126070427L;

	/**
	 * 批量删除-idList
	 */
	@ApiModelProperty(value = "批量删除-idList")
	@NotEmpty
	private List<String> idList;
}