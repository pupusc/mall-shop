package com.wanmi.sbc.bookmeta.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/10/14:47
 * @Description:
 */
@Data
public class SaleNum implements Serializable {
    @Column(name = "goods_id")
    private String spuId;
    @Column(name = "goods_name")
    private String spuName;
    @Column(name = "goods_info_id")
    private String skuId;
    @Column(name = "goods_info_name")
    private String skuName;
    @Column(name = "sales_num")
    private int salesNum;
    @Column(name = "fix_price")
    private double fixPrice;
}
