package com.wanmi.ares.report.employee.model.root;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>业务员业绩报表结构</p>
 * Created by of628-wenzhi on 2017-10-24-下午6:59.
 */
@Data
public class EmployeePerformanceReport implements Serializable {

    private static final long serialVersionUID = 5379732521511932349L;

    /**
     * 商户id
     */
    private String companyId;

    /**
     * 业务员id
     */
    private String employeeId;

    /**
     * 下单总笔数
     */
    private Long orderCount = 0L;

    /**
     * 下单总人数
     */
    private Long customerCount = 0L;

    /**
     * 订单总金额
     */
    private BigDecimal amount = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);

    /**
     * 付款总金额
     */
    private BigDecimal payAmount = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);

    /**
     * 付款订单数
     */
    private Long payCount = 0L;

    /**
     * 付款总人数
     */
    private Long payCustomerCount = 0L;


    /**
     * 退单总笔数
     */
    private Long returnCount = 0L;

    /**
     * 退单总金额
     */
    private BigDecimal returnAmount = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);

    /**
     * 退单总人数
     */
    private Long returnCustomerCount = 0L;

    /**
     * 笔单价
     */
    private BigDecimal orderUnitPrice = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);

    /**
     * 客单价
     */
    private BigDecimal customerUnitPrice = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);

    private LocalDate testCreateTime;

    private LocalDate targetDate;

}
