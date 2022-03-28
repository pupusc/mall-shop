package com.wanmi.sbc.customer.bean.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * t_counselor
 *
 * @author
 */
@Data
public class CounselorDto implements Serializable {
//    private Integer id;

    
    private Integer userId;

    /**
     * 上级id
     */
//    private Integer parentId;

    /**
     * 级别
     */
    
//    private Integer level;

    /**
     * 分销总数
     */
    
//    private Integer sellCount;

    /**
     * 分销商品总价
     */
    
//    private Long totalFee;

    /**
     * 状态 0:未激活 1:正常 2:解除  3:冻结
     */
    
//    private Integer status;

    /**
     * 评分
     */
    
//    private Integer score;

    /**
     * 等级进度
     */
    
//    private Integer levelRate;

    /**
     * 是否用等级进度 1:是 2:否
     */
    
//    private Integer levelRateFlag;

    /**
     * 是否发送晋升文案 1，是 2否
     */

//
//    private Integer delFlag;

    /**
     * 冻结金额
     */
    
//    private Integer freeze;

    /**
     * 可提现金额
     */
    
    private Integer profit;

    private static final long serialVersionUID = 1L;
}