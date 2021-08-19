package com.wanmi.sbc.crm.bean.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-10-15
 * \* Time: 19:27
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Data
@ApiModel
public class DataVo {

    private String name;

    private Object value;
}
