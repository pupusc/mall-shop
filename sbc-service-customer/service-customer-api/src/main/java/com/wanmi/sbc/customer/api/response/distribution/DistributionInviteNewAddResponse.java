package com.wanmi.sbc.customer.api.response.distribution;

import com.wanmi.sbc.customer.bean.vo.DistributionInviteNewVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 邀新记录新增结果
 * @Autho qiaokang
 * @Date：2019-03-04 15:18:59
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionInviteNewAddResponse implements Serializable {

    private static final long serialVersionUID = 1705311803247310283L;

    /**
     * 邀新记录信息
     */
    @ApiModelProperty(value = "邀新记录信息")
    DistributionInviteNewVo distributionInviteNewVo;

}
