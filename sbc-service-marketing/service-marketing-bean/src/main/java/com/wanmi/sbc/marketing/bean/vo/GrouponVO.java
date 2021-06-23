package com.wanmi.sbc.marketing.bean.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>拼团购VO</p>
 *
 * @author groupon
 * @date 2019-05-17 16:17:44
 */
@ApiModel
@Data
public class GrouponVO implements Serializable {

    private static final long serialVersionUID = -360656481325113486L;

    /**
     * 是否拼团购买
     */
    private Boolean grouponFlag;

    /**
     * 是否团长
     */
    private Boolean leader;


}