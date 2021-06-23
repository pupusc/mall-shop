package com.wanmi.sbc.setting.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>平台素材资源分类VO</p>
 * @author lq
 * @date 2019-11-05 16:14:55
 */
@ApiModel
@Data
public class SystemResourceCateVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 素材资源分类id
	 */
	@ApiModelProperty(value = "素材资源分类id")
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
	 * 分类图片
	 */
	@ApiModelProperty(value = "分类图片")
	private String cateImg;

	/**
	 * 分类层次路径,例1|01|001
	 */
	@ApiModelProperty(value = "分类层次路径,例1|01|001")
	private String catePath;

	/**
	 * 分类层级
	 */
	@ApiModelProperty(value = "分类层级")
	private Integer cateGrade;

	/**
	 * 拼音
	 */
	@ApiModelProperty(value = "拼音")
	private String pinYin;

	/**
	 * 简拼
	 */
	@ApiModelProperty(value = "简拼")
	private String spinYin;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 删除标识,0:未删除1:已删除
	 */
	@ApiModelProperty(value = "删除标识,0:未删除1:已删除")
	private DeleteFlag delFlag;

	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序")
	private Integer sort;

	/**
	 * 是否默认,0:否1:是
	 */
	@ApiModelProperty(value = "是否默认,0:否1:是")
	 private DefaultFlag isDefault;

}