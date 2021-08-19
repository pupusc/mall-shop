package com.wanmi.sbc.crm.rfmgroupstatistics.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * rfm_system_group_statistics
 * @author 
 */
@Data
public class RfmSystemGroupStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 会员人数
     */
    private Integer customerNum;

    /**
     * 访问数
     */
    private Integer uvNum;

    /**
     * 成交数
     */
    private Integer tradeNum;

    /**
     * 系统人群id
     */
    private Long systemGroupId;

    /**
     * 统计日期
     */
    private Date statDate;

    /**
     * 创建时间
     */
    private Date createTime;

}