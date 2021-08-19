package com.wanmi.sbc.message.bean.constant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName AliyunReviewStatusConstant
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/12/6 16:37
 **/
@ApiModel
@Data
public class AliyunReviewStatusConstant {

    /**
     * 审核中
     */
    @ApiModelProperty(value = "审核中")
    public static final String PENDINGREVIEW = "approving";

    /**
     * 审核通过
     */
    @ApiModelProperty(value = "审核通过")
    public static final String REVIEWPASS = "approved";

    /**
     * 审核未通过
     */
    @ApiModelProperty(value = "审核未通过")
    public static final String REVIEWFAILED = "rejected";

}
