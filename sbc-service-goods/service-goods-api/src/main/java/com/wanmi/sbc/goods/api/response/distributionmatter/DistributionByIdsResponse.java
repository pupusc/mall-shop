package com.wanmi.sbc.goods.api.response.distributionmatter;

import com.wanmi.sbc.goods.bean.vo.DistributionGoodsMatterPageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class DistributionByIdsResponse {

    @ApiModelProperty(value = "分销商品素材信息列表")
    private List<DistributionGoodsMatterPageVO> distributionGoodsMatterList;
}
