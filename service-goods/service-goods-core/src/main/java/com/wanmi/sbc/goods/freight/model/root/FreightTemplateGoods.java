package com.wanmi.sbc.goods.freight.model.root;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.goods.bean.enums.ValuationType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;


/**
 * 单品运费模板
 * Created by sunkun on 2018/5/2.
 */
@Data
@Entity
@Table(name = "freight_template_goods")
public class FreightTemplateGoods extends FreightTemplate{

    private static final long serialVersionUID = -5227483398368069267L;

    /**
     * 发货地-省份
     */
    @Column(name = "province_id")
    private Long provinceId;

    /**
     * 发货地-地市
     */
    @Column(name = "city_id")
    private Long cityId;

    /**
     * 发货地-区镇
     */
    @Column(name = "area_id")
    private Long areaId;

    /**
     * 发货地-街道
     */
    @Column(name = "street_id")
    private Long streetId;

    /**
     * 是否包邮(0:不包邮,1:包邮)
     */
    @Column(name = "freight_free_flag")
    private DefaultFlag freightFreeFlag;

    /**
     * 计价方式(0:按件数,1:按重量,2:按体积)
     */
    @Column(name = "valuation_type")
    private ValuationType valuationType;

    /**
     * 是否指定条件包邮(0:不指定,1:指定)
     */
    @Column(name = "specify_term_flag")
    private DefaultFlag specifyTermFlag;

    /**
     * 单品运费模板快递运送
     */
    @Transient
    private List<FreightTemplateGoodsExpress> freightTemplateGoodsExpresses;

    /**
     * 单品运费模板指定包邮条件
     */
    @Transient
    private List<FreightTemplateGoodsFree> freightTemplateGoodsFrees;

    /**
     * 与收货地匹配的单品运费模板
     * 用于运费计算,无需重复匹配收货地
     */
    @Transient
    private FreightTemplateGoodsExpress expTemplate;
}
