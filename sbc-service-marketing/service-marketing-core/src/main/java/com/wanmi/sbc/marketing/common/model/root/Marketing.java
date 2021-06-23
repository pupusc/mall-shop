package com.wanmi.sbc.marketing.common.model.root;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel;
import com.wanmi.sbc.marketing.bean.enums.MarketingScopeType;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.common.BaseBean;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 营销规则实体类
 */
@Data
@Entity
@Table(name = "marketing")
@NamedEntityGraph(name = "Marketing.Graph",attributeNodes = {@NamedAttributeNode("marketingScopeList")})
public class Marketing extends BaseBean {

    /**
     * 促销Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "marketing_id")
    private Long marketingId;

    /**
     * 促销名称
     */
    @Column(name = "marketing_name")
    private String marketingName;

    /**
     * 促销类型 0：满减 1:满折 2:满赠
     */
    @Column(name = "marketing_type")
    @Enumerated
    private MarketingType marketingType;

    /**
     * 促销子类型 0：满金额减 1：满数量减 2:满金额折 3:满数量折 4:满金额赠 5:满数量赠 6:一口价
     */
    @Column(name = "sub_type")
    @Enumerated
    private MarketingSubType subType;

    /**
     * 开始时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "begin_time")
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * 参加促销类型 0：全部货品 1：货品 2：品牌 3：分类
     */
    @Column(name = "scope_type")
    private MarketingScopeType scopeType;

    /**
     * 参加会员 -5:付费会员 -4:企业会员 -1:全部客户 0:全部等级  other:其他等级
     */
    @Column(name = "join_level")
    private String joinLevel;

    /**
     * 是否是商家，0：商家，1：boss
     */
    @Column(name = "is_boss")
    @Enumerated
    private BoolFlag isBoss;

    /**
     * 商家Id  0：boss,  other:其他商家
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 删除标记  0：正常，1：删除
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 是否暂停 0：正常，1：暂停
     */
    @Column(name = "is_pause")
    @Enumerated
    private BoolFlag isPause;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @Column(name = "create_person")
    private String createPerson;

    /**
     * 修改时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    @Column(name = "update_person")
    private String updatePerson;

    /**
     * 删除时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "delete_time")
    private LocalDateTime deleteTime;

    /**
     * 删除人
     */
    @Column(name = "delete_person")
    private String deletePerson;

    /**
     * 关联商品
     */
    @OneToMany
    @JoinColumn(name = "marketing_id", insertable = false, updatable = false)
    @JsonIgnore
    private List<MarketingScope> marketingScopeList;

    /**
     * joinLevel的衍射属性，获取枚举
     */
    @Transient
    private MarketingJoinLevel marketingJoinLevel;

    /**
     * joinLevel的衍射属性，marketingJoinLevel == LEVEL_LIST 时，可以获取对应的等级集合
     */
    @Transient
    private List<Long> joinLevelList;


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
