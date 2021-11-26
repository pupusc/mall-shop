package com.wanmi.sbc.crm.entity;

import lombok.Data;

import java.time.LocalDate;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-10-12
 * \* Time: 17:54
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Data
public class BaseParam {

    private String startTime;
    private String endTime;

    private String statDate;

    private Integer maxValue;
}
