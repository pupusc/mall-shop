package com.wanmi.sbc.crm.api.request.tagdimension;

import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>标签维度新增参数</p>
 * @author dyt
 * @date 2020-03-12 16:00:30
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDimensionAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 维度名
	 */
	@ApiModelProperty(value = "维度名")
	@Length(max=255)
	private String name;

	/**
	 * 维度对应表明
	 */
	@ApiModelProperty(value = "维度对应表明")
	@Length(max=255)
	private String tableName;

}