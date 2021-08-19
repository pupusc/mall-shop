package com.wanmi.sbc.crm.bean.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName RangeParamVo
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/10/9 11:32
 **/
@Data
public class RangeParamVo {

    /**
     * 范围类型：0：金额，1：时间
     */
    private Integer type;

    /**
     * 范围值
     */
    private List<Map<String,String>> dataValue;
}
