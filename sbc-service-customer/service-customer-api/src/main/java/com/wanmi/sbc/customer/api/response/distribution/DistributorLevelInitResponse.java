package com.wanmi.sbc.customer.api.response.distribution;

import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午2:13 2019/7/9
 * @Description:
 */
@ApiModel
@Data
public class DistributorLevelInitResponse {

    /**
     * 初始化的分销员等级
     */
    @ApiModelProperty(value = "初始化的分销员等级")
    private List<DistributorLevelVO> DistributorLevels = new ArrayList<>();

}
