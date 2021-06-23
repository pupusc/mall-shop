package com.wanmi.sbc.setting.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>系统日志VO</p>
 * @author guanfl
 * @date 2020-04-21 14:57:15
 */
@ApiModel
@Data
public class OperateDataLogVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@ApiModelProperty(value = "自增主键")
	private Long id;

	/**
	 * 操作内容
	 */
	@ApiModelProperty(value = "操作内容")
	private String operateContent;

	/**
	 * 操作标识
	 */
	@ApiModelProperty(value = "操作标识")
	private String operateId;

	/**
	 * 操作前数据
	 */
	@ApiModelProperty(value = "操作前数据")
	private String operateBeforeData;

	/**
	 * 操作后数据
	 */
	@ApiModelProperty(value = "操作后数据")
	private String operateAfterData;

	/**
	 * 操作人账号
	 */
	@ApiModelProperty(value = "操作人账号")
	private String operateAccount;

	/**
	 * 操作人名称
	 */
	@ApiModelProperty(value = "操作人名称")
	private String operateName;

	/**
	 * 操作时间
	 */
	@ApiModelProperty(value = "操作时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime operateTime;

	/**
	 * 删除标记
	 */
	@ApiModelProperty(value = "删除标记")
	private DeleteFlag delFlag;

}