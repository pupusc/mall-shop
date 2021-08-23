package com.wanmi.ares.source.model.root;

import com.wanmi.ares.enums.DataSourceType;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

/**
 * Created by dyt on 2017/9/18.
 */
@Data
public class GoodsInfoPool extends GoodsInfo {

    /**
     * 销售价
     */
    private BigDecimal price;

    /**
     * 销售件数
     */
    private Long num;

    /**
     * 状态分类
     */
    @Enumerated(EnumType.STRING)
    private DataSourceType type;
}
