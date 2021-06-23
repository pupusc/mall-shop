package com.wanmi.sbc.customer.api.response.level;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 客户等级统计响应
 * @Author: daiyitian
 * @Date: Created In 上午11:38 2017/11/14
 * @Description: 公司信息Response
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLevelCountResponse implements Serializable {

    private static final long serialVersionUID = 6492765528117007884L;

    /**
     * 客户等级数量
     */
    @ApiModelProperty(value = "客户等级数量")
    private long count;
}
