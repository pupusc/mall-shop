package com.wanmi.sbc.crm.bean.vo;

import lombok.Data;

import java.util.Date;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-13
 * \* Time: 14:54
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Data
public class CustomGroupVo {
    private Long id;

    /**
     * 分群名称
     */
    private String groupName;

    /**
     * 人群定义
     */
    private String definition;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updatePerson;

    /**
     * 分群信息
     */
    private CustomGroupDetailVo customGroupDetail;

    private Long customerCount;
}
