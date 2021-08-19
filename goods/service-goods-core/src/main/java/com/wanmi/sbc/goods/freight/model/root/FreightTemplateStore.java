package com.wanmi.sbc.goods.freight.model.root;

import com.wanmi.sbc.common.enums.DefaultFlag;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;

/**
 * 店铺运费模板
 * Created by sunkun on 2018/5/3.
 */
@Getter
@Setter
@Entity
@Table(name = "freight_template_store")
public class FreightTemplateStore extends FreightTemplate {

    private static final long serialVersionUID = 4198593697025361399L;

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
     * 运费计费规则(0:满金额包邮,1:固定运费)
     */
    @Column(name = "freight_type")
    private DefaultFlag freightType;

    /**
     * 满多少金额包邮
     */
    @Column(name = "satisfy_price")
    private BigDecimal satisfyPrice;

    /**
     * 不满金额的运费
     */
    @Column(name = "satisfy_freight")
    private BigDecimal satisfyFreight;

    /**
     * 固定的运费
     */
    @Column(name = "fixed_freight")
    private BigDecimal fixedFreight;

    /**
     * 店铺运费模板已选区域
     */
    @Transient
    private List<Long> selectedAreas;
}
