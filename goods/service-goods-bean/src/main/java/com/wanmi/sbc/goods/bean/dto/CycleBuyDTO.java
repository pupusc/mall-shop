package com.wanmi.sbc.goods.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.annotation.CanEmpty;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.DeliveryCycle;
import com.wanmi.sbc.goods.bean.enums.DeliveryPlan;
import com.wanmi.sbc.goods.bean.enums.GiftGiveMethod;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>周期购活动DTO</p>
 * @author weiwenhao
 * @date 2021-01-21 19:42:48
 */
@ApiModel
@Data
public class CycleBuyDTO  implements Serializable {


	private static final long serialVersionUID = 8473089753426283511L;
	/**
	 * 关联商品Id
	 */
	@ApiModelProperty(value = "关联商品Id")
	@NotNull
	private String originGoodsId;

	/**
	 * 创建商品ID
	 */
	@ApiModelProperty(value = "创建商品ID")
	private String goodsId;

	/**
	 * 周期购活动名称
	 */
	@Length(max = 40)
	@ApiModelProperty(value = "周期购活动名称")
	private String activityName;

	/**
	 * 活动文案
	 */
	@ApiModelProperty(value = "活动文案")
	private String recordActivities;


	/**
	 * 配送方案 0:商家主导配送 1:客户主导配送
	 */
	@ApiModelProperty(value = "配送方案 0:商家主导配送 1:客户主导配送")
	@NotNull
	private DeliveryPlan deliveryPlan;

	/**
	 * 赠送方式：0：默认全送 1:可选一种
	 */
	@ApiModelProperty(value = "赠送方式：0：默认全送 1:可选一种")
	@NotNull
	private GiftGiveMethod giftGiveMethod;

	/**
	 * 配送周期 0:每日一期 1:每周一期 2:每月一期
	 */
	@ApiModelProperty(value = "配送周期 0:每日一期 1:每周一期 2:每月一期")
	@NotNull
	private DeliveryCycle deliveryCycle;

	/**
	 * 发货日期:1.使用逗号拼接发送日期
	 */
	@ApiModelProperty(value = "发货日期:使用逗号拼接发送日期")
	private List<String> sendDateRule;

    /**
     * 发货日期: 使用逗号拼接发送日期
     */
    @ApiModelProperty(value = "发货日期:使用逗号拼接发送日期")
    private String sendDateRules;

	/**
	 * 周期购运费 =0:每期运费x期数， >0:满X期包邮
	 */
	@ApiModelProperty(value = "周期购运费 =0:每期运费x期数， >0:满X期包邮")
	@NotNull
	private Integer cycleFreightType;

	/**
	 * 上下架 0：上架 1:下架
	 */
	@ApiModelProperty(value = "上下架 0：上架 1:下架")
	private AddedFlag addedFlag;

	/**
	 * 商铺Id
	 */
	@ApiModelProperty(value = "商铺Id")
	private Long storeId;

	/**
	 * 删除标记  0：正常，1：删除
	 */
	@ApiModelProperty(value = "删除标记  0：正常，1：删除")
	private DeleteFlag delFlag;

	/**
	 * 关联赠品
	 */
	@ApiModelProperty(value = "关联赠品")
	private List<CycleBuyGiftDTO> cycleBuyGiftDTOList;


	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * createPerson
	 */
	@ApiModelProperty(value = "createPerson")
	private String createPerson;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * updatePerson
	 */
	@ApiModelProperty(value = "updatePerson")
	private String updatePerson;

	/**
	 * 删除时间
	 */
	@ApiModelProperty(value = "删除时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime deleteTime;

	/**
	 * deletePerson
	 */
	@ApiModelProperty(value = "deletePerson")
	private String deletePerson;


	/**
	 * 拼接发送日期
	 *
	 * @return
	 */
	public String getDateRule() {
		if (CollectionUtils.isNotEmpty(sendDateRule)) {
			StringBuffer buffer = new StringBuffer();
			sendDateRule.forEach(t -> {
				buffer.append(t).append(",");
			});
			if (buffer.length() > 0) {
				buffer.deleteCharAt(buffer.length() - 1);
			}
			return buffer.toString();
		}
		return null;
	}

}