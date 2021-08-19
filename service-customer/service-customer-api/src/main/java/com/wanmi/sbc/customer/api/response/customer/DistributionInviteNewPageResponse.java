package com.wanmi.sbc.customer.api.response.customer;
import com.wanmi.sbc.customer.bean.vo.DistributionInviteNewForPageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 邀请记录分页查询参数
 * Created by feitingting on 2019/2/21.
 */
@ApiModel
@Data
public class DistributionInviteNewPageResponse implements Serializable {
    /**
     * 会员分页
     */
    @ApiModelProperty(value = "邀新记录类，分页")
    private List<DistributionInviteNewForPageVO> recordList;

    /**
     * 总数
     */
    @ApiModelProperty(value = "总数")
    private Long total;

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页")
    private Integer currentPage;
}
