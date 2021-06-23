package com.wanmi.sbc.goods.bean.dto;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>VO</p>
 * @author 
 * @date 2020-08-17 14:46:43
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThirdGoodsCateDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 商品分类主键
	 */
	@ApiModelProperty(value = "商品分类主键")
	@NotNull
	private Long cateId;

	/**
	 * 分类名称
	 */
	@ApiModelProperty(value = "分类名称")
	@NotBlank
	private String cateName;

	/**
	 * 父分类ID
	 */
	@ApiModelProperty(value = "父分类ID")
	@NotNull
	private Long cateParentId;

	/**
	 * 分类层次路径,例0|01|001
	 */
	@ApiModelProperty(value = "分类层次路径,例0|01|001")
	@NotBlank
	private String catePath;

	/**
	 * 分类层级
	 */
	@ApiModelProperty(value = "分类层级")
	@NotNull
	private Integer cateGrade;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;
	/**
	 * 第三方平台来源(0,linkedmall)
	 */
	@ApiModelProperty(value = "第三方平台来源(0,linkedmall)")
	@NotNull
	private ThirdPlatformType thirdPlatformType;
	/**
	 * 删除标识,0:未删除1:已删除
	 *
	 */
	private DeleteFlag delFlag;
}