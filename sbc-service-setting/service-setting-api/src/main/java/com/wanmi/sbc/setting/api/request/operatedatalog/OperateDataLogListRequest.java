package com.wanmi.sbc.setting.api.request.operatedatalog;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>系统日志列表查询请求参数</p>
 * @author guanfl
 * @date 2020-04-21 14:57:15
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperateDataLogListRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-自增主键List
	 */
	@ApiModelProperty(value = "批量查询-自增主键List")
	private List<Long> idList;

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
	 * 搜索条件:操作时间开始
	 */
	@ApiModelProperty(value = "搜索条件:操作时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime operateTimeBegin;
	/**
	 * 搜索条件:操作时间截止
	 */
	@ApiModelProperty(value = "搜索条件:操作时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime operateTimeEnd;

	/**
	 * 删除标记
	 */
	@ApiModelProperty(value = "删除标记")
	private DeleteFlag delFlag;

}