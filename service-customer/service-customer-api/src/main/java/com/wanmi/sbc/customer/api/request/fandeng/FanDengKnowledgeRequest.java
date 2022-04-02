package com.wanmi.sbc.customer.api.request.fandeng;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @program: sbc-background
 * @description: 樊登积分查询入参
 * @author: Mr.Tian
 * @create: 2021-01-28 13:52
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FanDengKnowledgeRequest implements Serializable {

    private static final long serialVersionUID = -4710836069854992691L;

    /**
     * 会员编号
     */
    @ApiModelProperty(value = "会员编号")
    @NotBlank
    private String userNo;


}
