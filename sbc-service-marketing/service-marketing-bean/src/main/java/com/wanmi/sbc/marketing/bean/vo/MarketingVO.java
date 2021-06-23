package com.wanmi.sbc.marketing.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel;
import com.wanmi.sbc.marketing.bean.enums.MarketingScopeType;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-19 14:13
 */
@ApiModel
@Data
public class MarketingVO implements Serializable {

    private static final long serialVersionUID = 5114164129691782553L;
    /**
     * 营销Id
     */
    @ApiModelProperty(value = "营销Id")
    private Long marketingId;

    /**
     * 营销名称
     */
    @ApiModelProperty(value = "营销名称")
    private String marketingName;

    /**
     * 营销类型 0：满减 1:满折 2:满赠 3.组合套餐
     */
    @ApiModelProperty(value = "营销活动类型")
    private MarketingType marketingType;

    /**
     * 营销子类型 0：满金额减 1：满数量减 2:满金额折 3:满数量折 4:满金额赠 5:满数量赠 6.组合活动
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
    @ApiModelProperty(value = "参加营销类型")
    private MarketingScopeType scopeType;

    /**
     * 参加会员  -5:付费会员 -4:企业会员 -1:全部客户 0:全部等级  other:其他等级
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
    @ApiModelProperty(value = "商家Id，0：boss, other:其他商家")
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
    @ApiModelProperty(value = "营销关联商品列表")
    private List<MarketingScopeVO> marketingScopeList;

    /**
     * joinLevel的衍射属性，获取枚举
     */
    @ApiModelProperty(value = "关联客户等级")
    private MarketingJoinLevel marketingJoinLevel;

    /**
     * joinLevel的衍射属性，marketingJoinLevel == LEVEL_LIST 时，可以获取对应的等级集合
     */
    @ApiModelProperty(value = "关联其他等级的等级id集合")
    private List<Long> joinLevelList;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String storeName;

    /**
     * 等级名称
     */
    @ApiModelProperty(value = "等级名称")
    private String joinLevelName;
}
