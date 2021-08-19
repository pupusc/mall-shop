package com.wanmi.sbc.customer.api.request.levelrights;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.enums.LevelRightsType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>会员等级权益表新增参数</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerLevelRightsAddRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 权益名称
	 */
	@NotBlank
	@Length(max=50)
	private String rightsName;

	/**
	 * 权益类型 0等级徽章 1专属客服 2会员折扣 3券礼包 4返积分
	 */
	@NotNull
	private LevelRightsType rightsType;

	/**
	 * logo地址
	 */
	@Length(max=255)
	private String rightsLogo;

	/**
	 * 权益介绍
	 */
	private String rightsDescription;

	/**
	 * 权益规则(JSON)
	 */
	private String rightsRule;

	/**
	 * 活动Id
	 */
	private String activityId;

	/**
	 * 是否开启 0:关闭 1:开启
	 */
	private Integer status;

	/**
	 * 删除标识 0:未删除1:已删除
	 */
	private DeleteFlag delFlag;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	@Length(max=32)
	private String createPerson;

}