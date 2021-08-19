package com.wanmi.sbc.goods.api.request.bookingsale;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.PresellSaleStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>预售信息通用查询请求参数</p>
 * @author dany
 * @date 2020-06-05 10:47:21
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSaleQueryRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-idList
	 */
	@ApiModelProperty(value = "批量查询-idList")
	private List<Long> idList;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * 活动名称
	 */
	@ApiModelProperty(value = "活动名称")
	private String activityName;

	/**
	 * 店铺storeIdList
	 */
	@ApiModelProperty(value = "店铺storeIdList")
	private List<Long> storeIds;

	/**
	 * 商户id
	 */
	@ApiModelProperty(value = "商户id")
	private Long storeId;

	/**
	 * 预售类型 0：全款预售  1：定金预售
	 */
	@ApiModelProperty(value = "预售类型 0：全款预售  1：定金预售")
	private Integer bookingType;

	/**
	 * 搜索条件:定金支付开始时间开始
	 */
	@ApiModelProperty(value = "搜索条件:定金支付开始时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime handSelStartTimeBegin;
	/**
	 * 搜索条件:定金支付开始时间截止
	 */
	@ApiModelProperty(value = "搜索条件:定金支付开始时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime handSelStartTimeEnd;

	/**
	 * 搜索条件:定金支付结束时间开始
	 */
	@ApiModelProperty(value = "搜索条件:定金支付结束时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime handSelEndTimeBegin;
	/**
	 * 搜索条件:定金支付结束时间截止
	 */
	@ApiModelProperty(value = "搜索条件:定金支付结束时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime handSelEndTimeEnd;

	/**
	 * 搜索条件:尾款支付开始时间开始
	 */
	@ApiModelProperty(value = "搜索条件:尾款支付开始时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime tailStartTimeBegin;
	/**
	 * 搜索条件:尾款支付开始时间截止
	 */
	@ApiModelProperty(value = "搜索条件:尾款支付开始时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime tailStartTimeEnd;

	/**
	 * 搜索条件:尾款支付结束时间开始
	 */
	@ApiModelProperty(value = "搜索条件:尾款支付结束时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime tailEndTimeBegin;
	/**
	 * 搜索条件:尾款支付结束时间截止
	 */
	@ApiModelProperty(value = "搜索条件:尾款支付结束时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime tailEndTimeEnd;

	/**
	 * 搜索条件:预售开始时间开始
	 */
	@ApiModelProperty(value = "搜索条件:预售开始时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime bookingStartTimeBegin;
	/**
	 * 搜索条件:预售开始时间截止
	 */
	@ApiModelProperty(value = "搜索条件:预售开始时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime bookingStartTimeEnd;

	/**
	 * 搜索条件:预售结束时间开始
	 */
	@ApiModelProperty(value = "搜索条件:预售结束时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime bookingEndTimeBegin;
	/**
	 * 搜索条件:预售结束时间截止
	 */
	@ApiModelProperty(value = "搜索条件:预售结束时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime bookingEndTimeEnd;

	/**
	 * 发货日期 2020-01-10
	 */
	@ApiModelProperty(value = "发货日期 2020-01-10")
	private String deliverTime;

	/**
	 * 发货开始日期 2020-01-10
	 */
	@ApiModelProperty(value = "发货开始日期 2020-01-10")
	private String deliverStartTime;

	/**
	 * 发货结束日期 2020-01-10
	 */
	@ApiModelProperty(value = "发货结束日期 2020-01-10")
	private String deliverEndTime;

	/**
	 * 参加会员  -1:全部客户 0:全部等级 other:其他等级
	 */
	@ApiModelProperty(value = "参加会员  -1:全部客户 0:全部等级 other:其他等级")
	private String joinLevel;

	/**
	 * 是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）
	 */
	@ApiModelProperty(value = "是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）")
	private DefaultFlag joinLevelType;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	@ApiModelProperty(value = "是否删除标志 0：否，1：是")
	private DeleteFlag delFlag;

	/**
	 * 是否暂停 0:否 1:是
	 */
	@ApiModelProperty(value = "是否暂停 0:否 1:是")
	private Integer pauseFlag;

	/**
	 * 搜索条件:创建时间开始
	 */
	@ApiModelProperty(value = "搜索条件:创建时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeBegin;
	/**
	 * 搜索条件:创建时间截止
	 */
	@ApiModelProperty(value = "搜索条件:创建时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeEnd;

	/**
	 * 搜索条件:更新时间开始
	 */
	@ApiModelProperty(value = "搜索条件:更新时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeBegin;
	/**
	 * 搜索条件:更新时间截止
	 */
	@ApiModelProperty(value = "搜索条件:更新时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeEnd;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createPerson;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	private String updatePerson;

	/**
	 * 查询类型，0：全部，1：进行中，2：暂停中，3：未开始，4：已结束
	 */
	@ApiModelProperty(value = "查询类型，0：全部，1：进行中，2：暂停中，3：未开始，4：已结束")
	private PresellSaleStatus queryTab;

	/**
	 * 查询平台类型
	 */
	@ApiModelProperty(value = "查询平台类型")
	private Platform platform;

}