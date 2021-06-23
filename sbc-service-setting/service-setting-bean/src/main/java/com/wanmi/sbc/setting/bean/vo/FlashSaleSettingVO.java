package com.wanmi.sbc.setting.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>秒杀设置VO</p>
 * @author yxz
 * @date 2019-06-11 13:48:53
 */
@ApiModel
@Data
public class FlashSaleSettingVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 秒杀设置主键
	 */
	@ApiModelProperty(value = "秒杀设置主键")
	private Long id;

	/**
	 * 每日场次整点时间
	 */
	@ApiModelProperty(value = "每日场次整点时间")
	private String time;

	/**
	 * 是否启用 0：停用，1：启用
	 */
	@ApiModelProperty(value = "是否启用 0：停用，1：启用")
	private EnableStatus status;

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

	/**
	 * 是否关联未结束的秒杀商品
	 */
	@ApiModelProperty(value = "是否关联未结束的秒杀商品")
	private Boolean isFlashSale;

}