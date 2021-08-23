package com.wanmi.ares.report.customer.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>客户维度统计视图</p>
 * Created by of628-wenzhi on 2017-09-27-下午6:20.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerReport extends CustomerBaseReport {

    private static final long serialVersionUID = 6935436310851907693L;

    /**
     * 名称
     */
    private String name;

    /**
     * 账号
     */
    private String account;
}
