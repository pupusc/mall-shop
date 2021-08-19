package com.wanmi.sbc.goods.bean.vo;

import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>第三方平台类目VO</p>
 * @author 
 * @date 2020-08-29 13:35:42
 */
@ApiModel
@Data
public class ThirdGoodsCateVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * 三方商品分类主键
	 */
	@ApiModelProperty(value = "三方商品分类主键")
	private Long cateId;

	/**
	 * 分类名称
	 */
	@ApiModelProperty(value = "分类名称")
	private String cateName;

	/**
	 * 父分类ID
	 */
	@ApiModelProperty(value = "父分类ID")
	private Long cateParentId;

	/**
	 * 分类层次路径,例0|01|001
	 */
	@ApiModelProperty(value = "分类层次路径,例0|01|001")
	private String catePath;

	/**
	 * 分类层级
	 */
	@ApiModelProperty(value = "分类层级")
	private Integer cateGrade;

	/**
	 * 第三方平台来源(0,linkedmall)
	 */
	@ApiModelProperty(value = "第三方平台来源(0,linkedmall)")
	private Integer thirdPlatformType;

}