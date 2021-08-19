package com.wanmi.sbc.goods.api.response.distributionmatter;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.goods.bean.vo.DistributionGoodsMatterPageVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecDetailRelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel
@Data
public class DistributionGoodsMatterPageResponse {

    @ApiModelProperty(value = "分页分销商品素材信息")
    private MicroServicePage<DistributionGoodsMatterPageVO> distributionGoodsMatterPage;

    /**
     * 商品SKU的规格值全部数据
     */
    @ApiModelProperty(value = "商品SKU的规格值全部数据")
    private List<GoodsInfoSpecDetailRelVO> goodsInfoSpecDetails;

    /**
     * 商家信息数据
     */
    @ApiModelProperty(value = "商家信息数据")
    private List<CompanyInfoVO> companyInfoList = new ArrayList<>();
}
