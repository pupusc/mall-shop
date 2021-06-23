package com.wanmi.sbc.marketing.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.AuditStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>拼团活动信息表VO</p>
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@ApiModel
@Data
public class GrouponActivityVO implements Serializable {

	private static final long serialVersionUID = 7281972615555286334L;

	/**
	 * 活动ID
	 */
    @ApiModelProperty(value = "活动ID")
	private String grouponActivityId;

	/**
	 * 拼团人数
	 */
    @ApiModelProperty(value = "拼团人数")
	private Integer grouponNum;

	/**
	 * 开始时间
	 */
    @ApiModelProperty(value = "开始时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime startTime;

	/**
	 * 结束时间
	 */
    @ApiModelProperty(value = "结束时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime endTime;

	/**
	 * 拼团分类ID
	 */
    @ApiModelProperty(value = "拼团分类ID")
	private String grouponCateId;

	/**
	 * 是否自动成团
	 */
    @ApiModelProperty(value = "是否自动成团")
	private boolean autoGroupon;

	/**
	 * 是否包邮
	 */
    @ApiModelProperty(value = "是否包邮")
	private boolean freeDelivery;

	/**
	 * spu编号
	 */
    @ApiModelProperty(value = "spu编号")
	private String goodsId;

	/**
	 * spu商品名称
	 */
    @ApiModelProperty(value = "spu商品名称")
	private String goodsName;

	/**
	 * 店铺ID
	 */
    @ApiModelProperty(value = "店铺ID")
	private String storeId;

	/**
	 * 是否精选
	 */
	@ApiModelProperty(value = "是否精选")
	private Boolean sticky;

	/**
	 * 店铺名称
	 */
	@ApiModelProperty(value = "店铺名称")
	private String storeName;

	/**
	 * 已成团人数
	 */
    @ApiModelProperty(value = "已成团人数")
	private Integer alreadyGrouponNum;

	/**
	 * 待成团人数
	 */
    @ApiModelProperty(value = "待成团人数")
	private Integer waitGrouponNum;

	/**
	 * 团失败人数
	 */
    @ApiModelProperty(value = "团失败人数")
	private Integer failGrouponNum;

	/**
	 * 审核不通过原因
	 */
	@ApiModelProperty(value = "审核不通过原因")
	private String auditFailReason;

	/**
	 * 审核状态
	 */
	@ApiModelProperty(value = "活动审核状态，0：待审核，1：审核通过，2：审核不通过")
	private AuditStatus auditStatus;


	private DeleteFlag delFlag = DeleteFlag.NO;


	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;


	private String goodsNo;

}