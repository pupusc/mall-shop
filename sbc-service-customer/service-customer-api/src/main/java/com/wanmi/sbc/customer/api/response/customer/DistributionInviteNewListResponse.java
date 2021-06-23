package com.wanmi.sbc.customer.api.response.customer;

import com.wanmi.sbc.customer.bean.vo.DistributionInviteNewListVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 邀新记录列表查询返回体
 * @Autho qiaokang
 * @Date：2019-03-07 20:46:45
 */
@ApiModel
@Data
public class DistributionInviteNewListResponse implements Serializable {

    private static final long serialVersionUID = -8665986303254393632L;

    /**
     * 邀新记录列表
     */
    @ApiModelProperty(value = "邀新记录列表")
    List<DistributionInviteNewListVO> inviteNewListVOList;

}
