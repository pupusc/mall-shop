package com.wanmi.sbc.customer.api.response.customer;
import com.wanmi.sbc.customer.bean.vo.DistributionInviteNewForPageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 邀请记录导出查询参数
 * Created by of2975 on 2019/4/30.
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributionInviteNewExportResponse implements Serializable {
    /**
     * 邀新记录导出结果
     */
    @ApiModelProperty(value = "邀新记录导出结果")
    private List<DistributionInviteNewForPageVO> recordList;
}
