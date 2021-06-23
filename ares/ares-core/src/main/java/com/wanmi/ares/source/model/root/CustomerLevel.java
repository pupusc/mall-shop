package com.wanmi.ares.source.model.root;

import com.wanmi.ares.source.model.root.base.BaseData;
import lombok.*;

import java.math.BigDecimal;

/**
 * 客户等级基础信息
 * Created by sunkun on 2017/9/19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Document(indexName = EsConstants.ES_INDEX_BASIC, type = EsConstants.ES_TYPE_CUSTOMER_LEVEL)
@ToString(callSuper = true)
public class CustomerLevel extends BaseData {

    private static final long serialVersionUID = 5827226930884157269L;

    /**
     * 名称
     */
    private String name;

    /**
     * 折扣率
     */
    private BigDecimal discount;

    /**
     * 是否默认
     */
    @Builder.Default
    private boolean isDefault = false;

    /**
     * 商家id
     */
    private String companyId;

}
