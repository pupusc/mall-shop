package com.wanmi.sbc.goods.api.request.thirdgoodscate;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>第三方平台类目新增参数</p>
 * @author 
 * @date 2020-08-29 13:35:42
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdGoodsCateAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 三方商品分类主键
	 */
	@ApiModelProperty(value = "三方商品分类主键")
	@NotNull
	@Max(9223372036854775807L)
	private Long cateId;

	/**
	 * 分类名称
	 */
	@ApiModelProperty(value = "分类名称")
	@NotBlank
	@Length(max=45)
	private String cateName;

	/**
	 * 父分类ID
	 */
	@ApiModelProperty(value = "父分类ID")
	@Max(9223372036854775807L)
	private Long cateParentId;

	/**
	 * 分类层次路径,例0|01|001
	 */
	@ApiModelProperty(value = "分类层次路径,例0|01|001")
	@NotBlank
	@Length(max=1000)
	private String catePath;

	/**
	 * 分类层级
	 */
	@ApiModelProperty(value = "分类层级")
	@NotNull
	@Max(127)
	private Integer cateGrade;

	/**
	 * 第三方平台来源(0,linkedmall)
	 */
	@ApiModelProperty(value = "第三方平台来源(0,linkedmall)")
	@NotNull
	@Max(127)
	private Integer thirdPlatformType;

	/**
	 * 删除标识,0:未删除1:已删除
	 */
	@ApiModelProperty(value = "删除标识,0:未删除1:已删除", hidden = true)
	private DeleteFlag delFlag;

}