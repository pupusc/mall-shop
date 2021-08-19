package com.wanmi.sbc.crm.customergroup.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-12-23
 * \* Time: 10:10
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerGroupListResponse {

    private String groupId;
    private String groupName;
}
