package com.wanmi.ares.report.customer.model.response;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>客户等级分布视图</p>
 * Created by of628-wenzhi on 2017-10-16-上午11:33.
 */
@Data
public class CustomerLevelDistrView implements Serializable{

    private static final long serialVersionUID = -6856085844862718673L;

    /**
     * 商户id
     */
    private String companyId;

    /**
     * 客户等级id
     */
    private String levelId;

    /**
     * 客户等级名称
     */
    private String levelName;

    /**
     * 当前等级下客户数所占总客户数的百分比(包含'%')
     */
    private String centage;
}
