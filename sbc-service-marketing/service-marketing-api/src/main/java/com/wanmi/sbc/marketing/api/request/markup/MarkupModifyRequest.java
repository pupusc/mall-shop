package com.wanmi.sbc.marketing.api.request.markup;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.marketing.api.request.market.MarketingAddRequest;
import com.wanmi.sbc.marketing.bean.constant.MarketingErrorCode;
import com.wanmi.sbc.marketing.bean.enums.MarketingScopeType;
import com.wanmi.sbc.marketing.bean.vo.MarkupLevelVO;
import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotNull;

/**
 * <p>加价购活动修改参数</p>
 * @author he
 * @date 2021-02-04 16:09:09
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkupModifyRequest extends MarketingAddRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 加价购活动阶梯
	 */
	@ApiModelProperty(value = "活动名称")
	@NotNull
	private List<MarkupLevelVO> markupLevelList;
	public void valid() {
		Set set = new HashSet<BigDecimal>();
		// 校验参数，金额不能为空
		markupLevelList.stream().forEach((level) -> {
			if (level.getLevelAmount() == null) {
				throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
			}
			set.add(level.getLevelAmount());
			if (CollectionUtils.isEmpty(level.getMarkupLevelDetailList())) {
				throw new SbcRuntimeException(MarketingErrorCode.MARKETING_GIFT_TYPE_MIN_1);
			} else {
				level.getMarkupLevelDetailList().stream().forEach(detail -> {
					if (detail.getMarkupPrice()  == null   ) {
						throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
					}
				});
			}
		});

		if (set.size() != markupLevelList.size()) {
			throw new SbcRuntimeException(MarketingErrorCode.MARKETING_MULTI_LEVEL_AMOUNT_NOT_ALLOWED_SAME);
		}

		if (this.getScopeType() == MarketingScopeType.SCOPE_TYPE_CUSTOM) {
			if (this.getSkuIds() == null || this.getSkuIds().stream().allMatch(skuId -> "".equals(skuId.trim()))) {
				throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
			}
		}
		// 参加活动商品额换购商品不能一样
		List<String> markupSkuId = getMarkupLevelList().stream().flatMap(l -> l.getMarkupLevelDetailList().stream())
				.map(d -> d.getGoodsInfoId()).collect(Collectors.toList());

		markupSkuId.retainAll(getSkuIds());
		if(CollectionUtils.isNotEmpty(markupSkuId)){
			throw new SbcRuntimeException(MarketingErrorCode.MARKUP_GOODS_NOT_IN_MARKETING);
		}
	}
}