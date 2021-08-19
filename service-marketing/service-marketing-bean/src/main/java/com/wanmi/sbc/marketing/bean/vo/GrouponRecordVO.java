package com.wanmi.sbc.marketing.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>拼团活动参团信息表VO</p>
 * @author groupon
 * @date 2019-05-17 16:17:44
 */
@ApiModel
@Data
public class GrouponRecordVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * grouponRecordId
	 */
    @ApiModelProperty(value = "grouponRecordId")
	private String grouponRecordId;

	/**
	 * 拼团活动ID
	 */
    @ApiModelProperty(value = "拼团活动ID")
	private String grouponActivityId;

	/**
	 * 会员ID
	 */
    @ApiModelProperty(value = "会员ID")
	private String customerId;

	/**
	 * SPU编号
	 */
    @ApiModelProperty(value = "SPU编号")
	private String goodsId;

	/**
	 * sku编号
	 */
    @ApiModelProperty(value = "sku编号")
	private String goodsInfoId;

	/**
	 * 已购数量
	 */
    @ApiModelProperty(value = "已购数量")
	private Integer buyNum;

	/**
	 * 限购数量
	 */
    @ApiModelProperty(value = "限购数量")
	private Integer limitSellingNum;

	/**
	 * createTime
	 */
    @ApiModelProperty(value = "createTime")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * updateTime
	 */
    @ApiModelProperty(value = "updateTime")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

}