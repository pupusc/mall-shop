package com.wanmi.sbc.goods.bean.dto;

import com.wanmi.sbc.common.enums.ThirdPlatformType;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * <p>平台类目和第三方平台类目映射VO</p>
 * @author 
 * @date 2020-08-18 19:51:55
 */
@ApiModel
@Data
public class GoodsCateThirdCateRelDTO implements Serializable {
	private static final long serialVersionUID = 1L;


	/**
	 * 平台类目主键
	 */
	@ApiModelProperty(value = "平台类目主键")
	@NotNull
	private Long cateId;

	/**
	 * 第三方平台类目主键
	 */
	@ApiModelProperty(value = "第三方平台类目主键")
	@NotNull
	private Long thirdCateId;

	/**
	 * 第三方渠道(0，linkedmall)
	 */
	@ApiModelProperty(value = "第三方渠道(0，linkedmall)")
	@NotNull
	private ThirdPlatformType thirdPlatformType;

}