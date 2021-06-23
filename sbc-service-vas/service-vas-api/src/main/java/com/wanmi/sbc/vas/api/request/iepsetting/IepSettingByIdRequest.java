package com.wanmi.sbc.vas.api.request.iepsetting;

import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * <p>单个查询企业购设置请求参数</p>
 * @author 宋汉林
 * @date 2020-03-02 20:15:04
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IepSettingByIdRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 *  id 
	 */
	@ApiModelProperty(value = " id ")
	@NotNull
	private String id;

}