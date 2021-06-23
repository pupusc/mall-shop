package com.wanmi.sbc.customer.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>分销员等级</p>
 *
 * @author gaomuwei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributorLevelBaseVO implements Serializable {

    private static final long serialVersionUID = 8857314372930844164L;

    /**
     * 分销员等级id
     */
    private String distributorLevelId;


    /**
     * 分销员等级名称
     */
    private String distributorLevelName;

}