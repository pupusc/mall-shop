package com.wanmi.sbc.setting.api.request.evaluateratio;

import java.math.BigDecimal;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;

import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import org.apache.commons.lang3.Validate;
import org.hibernate.validator.constraints.*;

/**
 * <p>商品评价系数设置修改参数</p>
 * @author liutao
 * @date 2019-02-27 15:53:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EvaluateRatioModifyRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 系数id
	 */
	@Length(max=32)
	private String ratioId;

	/**
	 * 商品评论系数
	 */
	private BigDecimal goodsRatio;

	/**
	 * 服务评论系数
	 */
	private BigDecimal serverRatio;

	/**
	 * 物流评分系数
	 */
	private BigDecimal logisticsRatio;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	@Max(127)
	private Integer delFlag;

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

	/**
	 * 修改时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 修改人
	 */
	@Length(max=32)
	private String updatePerson;

	/**
	 * 删除时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime delTime;

	/**
	 * 删除人
	 */
	@Length(max=32)
	private String delPerson;

	@Override
	public void checkParam() {
		Validate.notNull(goodsRatio, ValidateUtil.NULL_EX_MESSAGE, "goodsRatio");
		Validate.notNull(serverRatio, ValidateUtil.NULL_EX_MESSAGE, "serverRatio");
		Validate.notNull(logisticsRatio, ValidateUtil.NULL_EX_MESSAGE, "logisticsRatio");
	}

}