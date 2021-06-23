package com.wanmi.sbc.goods.freight.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.ValuationType;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 单品运费模板快递运送
 * Created by sunkun on 2018/5/3.
 */
@Data
@Entity
@Table(name = "freight_template_goods_express")
public class FreightTemplateGoodsExpress {

    /**
     * 主键标识
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 运费模板id
     */
    @Column(name = "freight_temp_id")
    private Long freightTempId;

    /**
     * 配送地id(逗号分隔)
     */
    @Column(name = "destination_area")
    private String destinationArea;

    /**
     * 配送地名称(逗号分隔)
     */
    @Column(name = "destination_area_name")
    private String destinationAreaName;

    /**
     * 计价方式(0:按件数,1:按重量,2:按体积)
     */
    @Column(name = "valuation_type")
    private ValuationType valuationType;

    /**
     * 首件/重/体积
     */
    @Column(name = "freight_start_num")
    private BigDecimal freightStartNum;

    /**
     * 对应于首件/重/体积的起步价
     */
    @Column(name = "freight_start_price")
    private BigDecimal freightStartPrice;

    /**
     * 续件/重/体积
     */
    @Column(name = "freight_plus_num")
    private BigDecimal freightPlusNum;

    /**
     * 对应于续件/重/体积的价格
     */
    @Column(name = "freight_plus_price")
    private BigDecimal freightPlusPrice;

    /**
     * 是否默认(0:否,1:是)
     */
    @Column(name = "default_flag")
    private DefaultFlag defaultFlag;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 是否删除(0:否,1:是)
     */
    @Column(name = "del_flag")
    private DeleteFlag delFlag;

}
