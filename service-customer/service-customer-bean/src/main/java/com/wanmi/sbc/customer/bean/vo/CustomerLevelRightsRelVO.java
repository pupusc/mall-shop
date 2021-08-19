package com.wanmi.sbc.customer.bean.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>会员等级与权益关联表VO</p>
 *
 * @author yang
 * @since 2019/2/27
 */
@Data
public class CustomerLevelRightsRelVO implements Serializable {

    private static final long serialVersionUID = 7522876028361873735L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 用户等级id
     */
    private Long customerLevelId;

    /**
     * 权益id
     */
    private Integer rightsId;
}
