package com.wanmi.sbc.marketing.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel;
import com.wanmi.sbc.marketing.bean.enums.MarketingStatus;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-19 11:17
 */
@ApiModel
@Data
public class MarketingPageVO implements Serializable {

    private static final long serialVersionUID = -6883628601339940084L;
    /**
     * 营销Id
     */
    @ApiModelProperty(value = "营销Id")
    private Long marketingId;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String marketingName;

    /**
     * 活动类型
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
     * 目标客户
     */
    @ApiModelProperty(value = "参加会员", dataType = "com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel")
    private String joinLevel;

    /**
     * 是否暂停
     */
    @ApiModelProperty(value = "是否暂停")
    private BoolFlag isPause;

    /**
     * 活动状态
     */
    @ApiModelProperty(value = "活动状态")
    private MarketingStatus marketingStatus;

    /**
     * 关联商品
     */
    @ApiModelProperty(value = "关联商品列表")
    private List<MarketingScopeVO> marketingScopeList;

    /**
     * joinLevel的衍射属性，获取枚举
     */
    @ApiModelProperty(value = "关联客户等级")
    @Transient
    private MarketingJoinLevel marketingJoinLevel;

    /**
     * joinLevel的衍射属性，marketingJoinLevel == LEVEL_LIST 时，可以获取对应的等级集合
     */
    @ApiModelProperty(value = "关联其他等级的等级id集合")
    @Transient
    private List<Long> joinLevelList;

    /**
     * 获取活动状态
     *
     * @return
     */
    public MarketingStatus getMarketingStatus() {
        if (beginTime != null && endTime != null) {
            if (LocalDateTime.now().isBefore(beginTime)) {
                return MarketingStatus.NOT_START;
            } else if (LocalDateTime.now().isAfter(endTime)) {
                return MarketingStatus.ENDED;
            } else if (isPause == BoolFlag.YES) {
                return MarketingStatus.PAUSED;
            } else {
                return MarketingStatus.STARTED;
            }
        }
        return null;
    }

    /**
     * 营销规则
     */
    @ApiModelProperty(value = "关联营销规则列表")
    List<String> rulesList;

    /**
     * 店铺Id
     */
    @ApiModelProperty(value = "关联店铺Id")
    Long storeId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "关联店铺名称")
    String storeName;

    /**
     * 等级名称
     */
    @ApiModelProperty(value = "等级名称")
    private String levelName;
}
