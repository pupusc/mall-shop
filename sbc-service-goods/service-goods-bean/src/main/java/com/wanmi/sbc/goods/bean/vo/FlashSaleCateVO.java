package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>秒杀分类VO</p>
 * @author yxz
 * @date 2019-06-11 10:11:15
 */
@ApiModel
@Data
public class FlashSaleCateVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 秒杀分类主键
	 */
	@ApiModelProperty(value = "秒杀分类主键")
	private Long cateId;

	/**
	 * 分类名称
	 */
	@ApiModelProperty(value = "分类名称")
	private String cateName;

	/**
	 * 排序 默认0
	 */
	@ApiModelProperty(value = "排序 默认0")
	private Integer sort;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createPerson;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	private String updatePerson;

	/**
	 * 删除标识,0: 未删除 1: 已删除
	 */
	@ApiModelProperty(value = "删除标识,0: 未删除 1: 已删除")
	private DeleteFlag delFlag;

}