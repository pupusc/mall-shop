package com.wanmi.sbc.marketing.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.dto.MarketingGoodsInfoDTO;
import com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel;
import com.wanmi.sbc.marketing.bean.enums.MarketingScopeType;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>营销规则</p>
 * author: sunkun
 * Date: 2018-11-19
 */
@ApiModel
@Data
public class MarketingDTO {
    /**
     * 营销Id
     */
    @ApiModelProperty(value = "营销id")
    private Long marketingId;

    /**
     * 营销名称
     */
    @ApiModelProperty(value = "营销名称")
    private String marketingName;

    /**
     * 营销类型 0：满减 1:满折 2:满赠
     */
    @ApiModelProperty(value = "营销活动类型")
    private MarketingType marketingType;

    /**
     * 营销子类型 0：满金额减 1：满数量减 2:满金额折 3:满数量折 4:满金额赠 5:满数量赠
     */
    @ApiModelProperty(value = "营销子类型")
    private MarketingSubType subType;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 参加营销类型 0：全部货品 1：货品 2：品牌 3：分类
     */
    @ApiModelProperty(value = "参加营销范围")
    private MarketingScopeType scopeType;

    /**
     * 参加会员  -5:付费会员 -4:企业会员 0:全部等级 -1:全部用户 other:其他等级
     */
    @ApiModelProperty(value = "参加会员", dataType = "com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel")
    private String joinLevel;

    /**
     * 是否是商家，0：商家，1：boss
     */
    @ApiModelProperty(value = "是否是商家")
    private BoolFlag isBoss;

    /**
     * 商家Id  0：boss,  other:其他商家
     */
    @ApiModelProperty(value = "商家Id，0：boss,  other:其他商家")
    private Long storeId;

    /**
     * 删除标记  0：正常，1：删除
     */
    @ApiModelProperty(value = "是否已删除")
    private DeleteFlag delFlag;

    /**
     * 是否暂停 0：正常，1：暂停
     */
    @ApiModelProperty(value = "是否暂停")
    private BoolFlag isPause;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createPerson;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updatePerson;

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime deleteTime;

    /**
     * 删除人
     */
    @ApiModelProperty(value = "删除人")
    private String deletePerson;

    /**
     * 关联商品
     */
    @ApiModelProperty(value = "关联商品范围列表")
    private List<MarketingScopeDTO> marketingScopeList;

    /**
     * 满减等级
     */
    @ApiModelProperty(value = "营销满减多级优惠列表")
    private List<MarketingFullReductionLevelDTO> fullReductionLevelList;

    /**
     * 满折等级
     */
    @ApiModelProperty(value = "营销满折多级优惠列表")
    private List<MarketingFullDiscountLevelDTO> fullDiscountLevelList;

    /**
     * 满赠等级
     */
    @ApiModelProperty(value = "营销满赠多级优惠列表")
    private List<MarketingFullGiftLevelDTO> fullGiftLevelList;

    /**
     * 营销关联商品
     */
    @ApiModelProperty(value = "营销关联商品")
    private MarketingGoodsInfoDTO goodsList;

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
