package com.wanmi.sbc.customer.api.response.distribution;

import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午5:21 2019/6/13
 * @Description:
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributorLevelListAllResponse implements Serializable {

    private static final long serialVersionUID = -2401577978393772199L;

    /**
     * 分销员等级列表
     */
    @ApiModelProperty(value = "分销员等级列表")
    private List<DistributorLevelVO> distributorLevelList;

}
