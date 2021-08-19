package com.wanmi.sbc.goods.bean.dto;

import com.wanmi.sbc.common.enums.ThirdPlatformType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>VO</p>
 * @author 
 * @date 2020-08-17 14:46:43
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThirdGoodsCateRelDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("主键")
	private Long id;
	@ApiModelProperty("第三方类目id")
	private Long thirdCateId;
	@ApiModelProperty("第三方类目名称")
	private String thirdCateName;
	@ApiModelProperty("第三方类目父id")
	private Long thirdCateParentId;
	@ApiModelProperty("渠道来源")
	private ThirdPlatformType thirdPlatformType;
	@ApiModelProperty("平台类目id")
	private Long cateId;
	@ApiModelProperty("平台类目名称")
	private String cateName;

}