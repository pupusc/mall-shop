package com.wanmi.sbc.crm.bean.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-10-15
 * \* Time: 19:23
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Data
@ApiModel
public class RfmStatisticVo {

    private String title;

    private int type;

    private List<DataVo> data;
}
