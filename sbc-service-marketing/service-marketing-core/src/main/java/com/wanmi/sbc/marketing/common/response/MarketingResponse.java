package com.wanmi.sbc.marketing.common.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoResponseVO;
import com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel;
import com.wanmi.sbc.marketing.bean.enums.MarketingScopeType;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.buyoutprice.model.entry.MarketingBuyoutPriceLevel;
import com.wanmi.sbc.marketing.common.model.root.MarketingScope;
import com.wanmi.sbc.marketing.discount.model.entity.MarketingFullDiscountLevel;
import com.wanmi.sbc.marketing.gift.model.root.MarketingFullGiftLevel;
import com.wanmi.sbc.marketing.halfpricesecondpiece.model.entry.MarketingHalfPriceSecondPieceLevel;
import com.wanmi.sbc.marketing.markup.model.root.MarkupLevel;
import com.wanmi.sbc.marketing.reduction.model.entity.MarketingFullReductionLevel;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Convert;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MarketingResponse{

    /**
     * 促销Id
     */
    private Long marketingId;

    /**
     * 促销名称
     */
    private String marketingName;

    /**
     * 促销类型 0：满减 1:满折 2:满赠
     */
    private MarketingType marketingType;

    /**
     * 促销子类型 0：满金额减 1：满数量减 2:满金额折 3:满数量折 4:满金额赠 5:满数量赠
     */
    private MarketingSubType subType;

    /**
     * 开始时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 参加促销类型 0：全部货品 1：货品 2：品牌 3：分类
     */
    private MarketingScopeType scopeType;

    /**
     * 参加会员 0:全部等级 -1:全部用户 other:其他等级
     */
    private String joinLevel;

    /**
     * 是否是商家，0：商家，1：boss
     */
    private BoolFlag isBoss;

    /**
     * 商家Id  0：boss,  other:其他商家
     */
    private Long storeId;

    /**
     * 删除标记  0：正常，1：删除
     */
    private DeleteFlag delFlag;

    /**
     * 是否暂停 0：正常，1：暂停
     */
    private BoolFlag isPause;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 修改时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    private String updatePerson;

    /**
     * 删除时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime deleteTime;

    /**
     * 删除人
     */
    private String deletePerson;

    /**
     * 关联商品
     */
    private List<MarketingScope> marketingScopeList;

    /**
     * 满减等级
     */
    private List<MarketingFullReductionLevel> fullReductionLevelList;

    /**
     * 满折等级
     */
    private List<MarketingFullDiscountLevel> fullDiscountLevelList;

    /**
     * 满赠等级
     */
    private List<MarketingFullGiftLevel> fullGiftLevelList;

    /**
     * 一口价等级
     */

    private List<MarketingBuyoutPriceLevel> buyoutPriceLevelList;

    /**
     * 第二件半价
     */
    private List<MarketingHalfPriceSecondPieceLevel> halfPriceSecondPieceLevel;

    /**
     * 加价购
     */
    private List<MarkupLevel> markupLevelList;
    /**
     * 营销关联商品
     */
    private GoodsInfoResponseVO goodsList;

    /**
     * 商家名称
     */
    private String storeName;

    public MarketingJoinLevel getMarketingJoinLevel() {
        if(joinLevel.equals("-5")){
            return MarketingJoinLevel.PAID_CARD_CUSTOMER;
        }else if(joinLevel.equals("-4")){
            return MarketingJoinLevel.ENTERPRISE_CUSTOMER;
        }else if(joinLevel.equals("0")){
            return MarketingJoinLevel.ALL_LEVEL;
        }else if(joinLevel.equals("-1")){
            return MarketingJoinLevel.ALL_CUSTOMER;
        }else{
            return MarketingJoinLevel.LEVEL_LIST;
        }
    }

    public List<Long> getJoinLevelList() {
        return Arrays.stream(joinLevel.split(",")).map(Long::parseLong).collect(Collectors.toList());
    }

}
