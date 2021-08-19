package com.wanmi.sbc.customer.api.request.distribution;

import com.wanmi.sbc.customer.bean.dto.DistributorLevelDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午5:32 2019/6/13
 * @Description:
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributorLevelBatchSaveRequest {

    /**
     * 分销员等级列表
     */
    @ApiModelProperty(value = "分销员等级列表")
    @NotEmpty
    private List<DistributorLevelDTO> distributorLevelList;

}
