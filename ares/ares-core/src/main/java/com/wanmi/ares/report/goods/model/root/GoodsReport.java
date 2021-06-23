package com.wanmi.ares.report.goods.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品报表公共信息
 * Created by daiyitian on 2017/9/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsReport implements Serializable{

    /**
     * 编号
     */
    @Id
    private String id;

    /**
     * 时间（含月份、年份）
     */
    private String date;

    /**
     * 下单笔数
     */
    private Long orderCount = 0L;

    /**
     * 下单总金额
     */
    private BigDecimal orderAmt = BigDecimal.ZERO;

    /**
     * 下单件数
     */
    private Long orderNum = 0L;

    /**
     * 付款笔数
     */
    private Long payCount = 0L;

    /**
     * 付款件数
     */
    private Long payNum = 0L;

    /**
     * 付款金额
     */
    private BigDecimal payAmt = BigDecimal.ZERO;

    /**
     * 退单笔数
     */
    private Long returnOrderCount = 0L;

    /**
     * 退单金额
     */
    private BigDecimal returnOrderAmt = BigDecimal.ZERO;

    /**
     * 退单件数
     */
    private Long returnOrderNum = 0L;

    /**
     * 商户ID
     */
    private String companyId;

    /**
     * 是否叶子节点
     * 仅分类模块使用
     */
    private Integer isLeaf = 0;
}
