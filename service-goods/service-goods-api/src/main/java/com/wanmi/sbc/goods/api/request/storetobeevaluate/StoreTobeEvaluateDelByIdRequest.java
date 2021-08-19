package com.wanmi.sbc.goods.api.request.storetobeevaluate;

import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>单个删除店铺服务待评价请求参数</p>
 * @author lzw
 * @date 2019-03-20 17:01:46
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreTobeEvaluateDelByIdRequest implements Serializable {

	private static final long serialVersionUID = -1975913197319683761L;
	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	@NotNull
	private String id;
}