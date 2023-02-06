package com.wanmi.sbc.topic.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TopicCustomerPointsResponse implements Serializable {
    private static final long serialVersionUID = -5138062021719883495L;


    @ApiModelProperty("会员id")
    private String customer_id;

    @ApiModelProperty("会员name")
    private String customer_name;

    @ApiModelProperty("可用积分")
    private Long points_available;

    @ApiModelProperty("说明文本")
    private String points_text;

}
