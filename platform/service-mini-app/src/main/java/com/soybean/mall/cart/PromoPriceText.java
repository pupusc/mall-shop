package com.soybean.mall.cart;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullDiscountLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullReductionLevelVO;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Liang Jun
 * @desc 商品促销文案
 * @date 2022-07-09 23:04:00
 */
public class PromoPriceText {
    /**
     * 促销文案
     */
    public static TextResult promoText(List<TextParam$Sku> skus, TextParam$Mkt mktBO) {
        TextResult result = new TextResult();
        if (CollectionUtils.isEmpty(skus) || mktBO == null) {
            return result;
        }

        long totalCount = skus.stream().mapToLong(i -> i.getCount()).sum();
        BigDecimal totalPrice = skus.stream().map(i->i.getSalePrice().multiply(BigDecimal.valueOf(i.getCount()))).reduce(BigDecimal.ZERO, BigDecimal::add);

        String text = ""; //促销展示文案
        Boolean imax = true; //促销是否最大

        if (MarketingSubType.REDUCTION_FULL_COUNT.equals(mktBO.getSubType())) { //满数量减
            if (mktBO.getFullReductionLevelList() == null) {
                return result;
            }
            for (int i = 0; i < mktBO.getFullReductionLevelList().size(); i++) {
                MarketingFullReductionLevelVO item = mktBO.getFullReductionLevelList().get(i);
                imax = totalCount >= item.getFullCount(); //是否满足
                if (!imax) {
                    if (i == 0) {
                        text = "满" + item.getFullCount() + "件减" + item.getReduction() + "元";
                    }
                    text += "，再买" + (item.getFullCount() - totalCount) + "件减" + item.getReduction();
                    break;
                }
                text = "已满" + item.getFullCount() + "件减" + item.getReduction() + "元";
            }
        } else if (MarketingSubType.REDUCTION_FULL_AMOUNT.equals(mktBO.getSubType())) { //满金额减
            if (mktBO.getFullReductionLevelList() == null) {
                return result;
            }
            for (int i = 0; i < mktBO.getFullReductionLevelList().size(); i++) {
                MarketingFullReductionLevelVO item = mktBO.getFullReductionLevelList().get(i);
                imax = totalPrice.compareTo(item.getFullAmount()) >= 0;
                if (!imax) {
                    if (i == 0) {
                        text = "满" + item.getFullAmount() + "元减" + item.getReduction() + "元";
                    }
                    text += "，再买" + item.getFullAmount().subtract(totalPrice) + "元减" + item.getReduction();
                    break;
                }
                text = "已满" + item.getFullAmount() + "元减" + item.getReduction() + "元";
            }
        } else if (MarketingSubType.DISCOUNT_FULL_COUNT.equals(mktBO.getSubType())) { //满数量折
            if (mktBO.getFullDiscountLevelList() == null) {
                return result;
            }
            for (int i = 0; i < mktBO.getFullDiscountLevelList().size(); i++) {
                MarketingFullDiscountLevelVO item = mktBO.getFullDiscountLevelList().get(i);
                imax = totalCount >= item.getFullCount();
                if (!imax) {
                    if (i == 0) {
                        text = "满" + item.getFullCount() + "件打" + item.getDiscount() + "折";
                    }
                    text = text + "，再买" + (item.getFullCount() - totalCount) + "件打" + item.getDiscount() + "折";
                    break;
                }
                text = "已满" + item.getFullCount() + "件打" + item.getDiscount() + "折";
            }
        } else if (MarketingSubType.DISCOUNT_FULL_AMOUNT.equals(mktBO.getSubType())) { //满金额折
            if (mktBO.getFullDiscountLevelList() == null) {
                return result;
            }
            for (int i = 0; i < mktBO.getFullDiscountLevelList().size(); i++) {
                MarketingFullDiscountLevelVO item = mktBO.getFullDiscountLevelList().get(i);
                imax = totalPrice.compareTo(item.getFullAmount()) >= 0;
                if (!imax) {
                    if (i == 0) {
                        text = "满" + item.getFullAmount() + "元打" + item.getDiscount() + "折";
                    }
                    text = text + "，再买" + item.getFullAmount().subtract(totalPrice) + "元打" + item.getDiscount() + "折";
                    break;
                }
                text = "已满" + item.getFullAmount() + "元打" + item.getDiscount() + "折";
            }
        } else {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "暂时不支持的营销活动");
        }

        result.setText(text);
        result.setMaxPolicy(imax);
        return result;
    }

    @Data
    public static class TextParam$Sku {
        private String skuId;
        private Long count;
        private BigDecimal salePrice;
    }

    @Data
    public static class TextParam$Mkt {
        /**
         * 营销类型
         */
        private MarketingSubType subType;
        /**
         * 满减
         */
        private List<MarketingFullReductionLevelVO> fullReductionLevelList;
        /**
         * 满折
         */
        private List<MarketingFullDiscountLevelVO> fullDiscountLevelList;
    }

    @Data
    public static class TextResult {
        /**
         * 促销文案
         */
        private String text;
        /**
         * 是否最大优惠
         */
        private Boolean maxPolicy = true;
    }
}
