package com.wanmi.sbc.goods.api.request.storetobeevaluate;

import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>单个查询店铺服务待评价请求参数</p>
 * @author lzw
 * @date 2019-03-20 17:01:46
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreTobeEvaluateByIdRequest implements Serializable {

	private static final long serialVersionUID = -2444649617461781887L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	@NotNull
	private String id;
}