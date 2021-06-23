package com.wanmi.sbc.customer.bean.vo;

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
 * <p>用户签到记录VO</p>
 * @author wangtao
 * @date 2019-10-05 16:13:04
 */
@ApiModel
@Data
public class CustomerSignRecordVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户签到记录表id
	 */
	@ApiModelProperty(value = "用户签到记录表id")
	private String signRecordId;

	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id")
	private String customerId;

	/**
	 * 签到日期记录
	 */
	@ApiModelProperty(value = "签到日期记录")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime signRecord;

	/**
	 * 删除区分：0 未删除，1 已删除
	 */
	@ApiModelProperty(value = "删除区分：0 未删除，1 已删除")
	private DeleteFlag delFlag;

}