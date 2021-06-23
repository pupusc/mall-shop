package com.wanmi.sbc.marketing.markup.model.root;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wanmi.sbc.marketing.bean.vo.MarkupLevelDetailVO;
import com.wanmi.sbc.marketing.gift.model.root.MarketingFullGiftDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>加价购活动实体类</p>
 * @author he
 * @date 2021-02-04 16:11:01
 */
@Data
@Entity
@Table(name = "markup_level")
public class MarkupLevel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 加价购阶梯id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 加价购id
	 */
	@Column(name = "markup_id")
	private Long markupId;

	/**
	 * 加价购阶梯满足金额
	 */
	@Column(name = "level_amount")
	private BigDecimal levelAmount;

	/**
	 * 加价购活动阶梯详情
	 */
	@OneToMany
	@JoinColumn(name = "markup_level_id", insertable = false, updatable = false)
	@JsonIgnore
	private List<MarkupLevelDetail> markupLevelDetailList;
}