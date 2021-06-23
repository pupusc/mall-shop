package com.wanmi.sbc.goods.api.request.flashsalecate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Max;
import java.time.LocalDateTime;

/**
 * <p>秒杀分类新增参数</p>
 * @author yxz
 * @date 2019-06-11 10:11:15
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleCateAddRequest extends GoodsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 分类名称
	 */
	@ApiModelProperty(value = "分类名称")
	@NotBlank
	@Length(max=45)
	private String cateName;

	/**
	 * 排序 默认0
	 */
	@ApiModelProperty(value = "排序 默认0")
	@Max(127)
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
	@Length(max=32)
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
	@Length(max=32)
	private String updatePerson;

	/**
	 * 删除标识,0: 未删除 1: 已删除
	 */
	@ApiModelProperty(value = "删除标识,0: 未删除 1: 已删除")
	private DeleteFlag delFlag;

}