package com.wanmi.sbc.crm.customerlevel.model;

import lombok.*;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-13
 * \* Time: 16:30
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLevel {

    private Long customerLevelId;

    private String customerLevelName;

    private List<Long> customerLevelIdList;
}
