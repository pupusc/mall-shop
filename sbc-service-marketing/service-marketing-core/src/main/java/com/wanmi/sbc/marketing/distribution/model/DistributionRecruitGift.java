package com.wanmi.sbc.marketing.distribution.model;


import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * <p>礼包商品实体类</p>
 * @author gaomuwei
 * @date 2019-02-19 10:37:20
 */
@Data
@Entity
@Table(name = "distribution_recruit_gift")
public class DistributionRecruitGift implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "recruit_gift_id")
	private String recruitGiftId;

	/**
	 * 单品id
	 */
	@Column(name = "goods_info_id")
	private String goodsInfoId;

}