package com.wanmi.sbc.distribute;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.distributionmatter.DistributionGoodsMatterProvider;
import com.wanmi.sbc.goods.api.provider.distributionmatter.DistributionGoodsMatterQueryProvider;
import com.wanmi.sbc.goods.api.request.distributionmatter.DistributionGoodsMatterPageRequest;
import com.wanmi.sbc.goods.api.request.distributionmatter.UpdateRecommendNumRequest;
import com.wanmi.sbc.goods.api.response.distributionmatter.DistributionGoodsMatterPageResponse;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;

import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "DistributionGoodsMatterController", description = "分销商品素材")
@RestController
@RequestMapping("/distribution/goods-matter")
@Validated
public class DistributionGoodsMatterController {

    @Autowired
    private DistributionGoodsMatterQueryProvider distributionGoodsMatterQueryProvider;

    @Autowired
    private DistributionGoodsMatterProvider distributionGoodsMatterProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "分页分销商品素材")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<DistributionGoodsMatterPageResponse> page(@RequestBody @Valid DistributionGoodsMatterPageRequest distributionGoodsMatterPageRequest){
        BaseResponse<DistributionGoodsMatterPageResponse> response = distributionGoodsMatterQueryProvider.page(distributionGoodsMatterPageRequest);
        return response;
    }


    @ApiOperation(value = "统计某个SKU分销素材分享次数")
    @RequestMapping(value = "/matter-recommend-num", method = RequestMethod.POST)
    public BaseResponse deleteList(@RequestBody @Valid UpdateRecommendNumRequest updateRecommendNumRequest){
        distributionGoodsMatterProvider.updataRecomendNumById(updateRecommendNumRequest);
        return BaseResponse.SUCCESSFUL();
    }


    @ApiOperation(value = "分页查询分销商品素材")
    @RequestMapping(value = "/page/new", method = RequestMethod.POST)
    public BaseResponse<DistributionGoodsMatterPageResponse> pageNew(@RequestBody DistributionGoodsMatterPageRequest distributionGoodsMatterPageRequest){
        //获取登录人ID
        String customerId = commonUtil.getOperatorId();

        if (StringUtils.isNotBlank(customerId)){
            distributionGoodsMatterPageRequest.setCustomerId(customerId);
        }
        distributionGoodsMatterPageRequest.setDistributionGoodsAudit(DistributionGoodsAudit.CHECKED);
        BaseResponse<DistributionGoodsMatterPageResponse> response = distributionGoodsMatterQueryProvider.pageNew(distributionGoodsMatterPageRequest);
        return response;
    }
}
