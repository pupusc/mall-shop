package com.wanmi.sbc.customer.api.response.company;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 公司信息待审核数量响应
 * @Author: daiyitian
 * @Date: Created In 上午11:38 2017/11/14
 * @Description: 公司信息Response
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyWaitCheckCountResponse implements Serializable {

    private static final long serialVersionUID = -8702105223773593424L;
    /**
     * 公司信息待审核数量
     */
    @ApiModelProperty(value = "公司信息待审核数量")
    private Long count;

}
