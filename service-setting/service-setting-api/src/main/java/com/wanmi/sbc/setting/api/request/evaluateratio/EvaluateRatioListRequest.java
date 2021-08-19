package com.wanmi.sbc.setting.api.request.evaluateratio;

import java.math.BigDecimal;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import java.util.List;

/**
 * <p>商品评价系数设置列表查询请求参数</p>
 * @author liutao
 * @date 2019-02-27 15:53:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateRatioListRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-系数idList
	 */
	private List<String> ratioIdList;

	/**
	 * 系数id
	 */
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
	private Integer delFlag;

	/**
	 * 搜索条件:创建时间开始
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeBegin;
	/**
	 * 搜索条件:创建时间截止
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeEnd;

	/**
	 * 创建人
	 */
	private String createPerson;

	/**
	 * 搜索条件:修改时间开始
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeBegin;
	/**
	 * 搜索条件:修改时间截止
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeEnd;

	/**
	 * 修改人
	 */
	private String updatePerson;

	/**
	 * 搜索条件:删除时间开始
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime delTimeBegin;
	/**
	 * 搜索条件:删除时间截止
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime delTimeEnd;

	/**
	 * 删除人
	 */
	private String delPerson;

}