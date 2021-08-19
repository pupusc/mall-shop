package com.wanmi.sbc.marketing.api.request.grouponactivity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.GrouponTabTypeStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>拼团活动信息表列表查询请求参数</p>
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponActivityListRequest  {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-活动IDList
	 */
	private List<String> grouponActivityIdList;

	/**
	 * 活动ID
	 */
	private String grouponActivityId;

	/**
	 * 拼团人数
	 */
	private Integer grouponNum;

	/**
	 * 搜索条件:开始时间开始
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime startTimeBegin;
	/**
	 * 搜索条件:开始时间截止
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime startTimeEnd;

	/**
	 * 搜索条件:结束时间开始
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime endTimeBegin;
	/**
	 * 搜索条件:结束时间截止
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime endTimeEnd;

	/**
	 * 拼团分类ID
	 */
	private String grouponCateId;

	/**
	 * 是否自动成团，0：否，1：是
	 */
	private Byte autoGroupon;

	/**
	 * 是否包邮，0：否，1：是
	 */
	private Byte freeDelivery;

	/**
	 * spu编号
	 */
	private String goodsId;

	/**
	 * spu商品名称
	 */
	private String goodsName;

	/**
	 * 店铺ID
	 */
	private String storeId;

	/**
	 * 是否精选，0：否，1：是
	 */
	private Byte isTop;

	/**
	 * 是否审核通过，0：待审核，1：审核通过，2：审核不通过
	 */
	private String status;

	/**
	 * 审核不通过原因
	 */
	private String failReason;

	/**
	 * 已成团人数
	 */
	private Integer alreadyGrouponNum;

	/**
	 * 待成团人数
	 */
	private Integer waitGrouponNum;

	/**
	 * 团失败人数
	 */
	private Integer failGrouponNum;


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
	 * 搜索条件:更新时间开始
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeBegin;
	/**
	 * 搜索条件:更新时间截止
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeEnd;



	/**
	 * 删除标记 0未删除 1已删除
	 */
	@ApiModelProperty(value = "删除标记")
	private DeleteFlag delFlag = DeleteFlag.NO;

	/**
	 *  页面tab类型，0: 即将开始, 1: 进行中, 2: 已结束，3：待审核，4：审核失败
	 */
	@ApiModelProperty(value = " 页面tab类型")
	private GrouponTabTypeStatus tabType;


	/**
	 * 批量查询-spu
	 */
	private List<String> spuIdList;

}