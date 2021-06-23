package com.wanmi.sbc.marketing;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.marketing.api.provider.buyoutprice.MarketingBuyoutPriceProvider;
import com.wanmi.sbc.marketing.api.provider.buyoutprice.MarketingBuyoutPriceQueryProvider;
import com.wanmi.sbc.marketing.api.request.buyoutprice.MarketingBuyoutPriceAddRequest;
import com.wanmi.sbc.marketing.api.request.buyoutprice.MarketingBuyoutPriceSearchRequest;
import com.wanmi.sbc.marketing.api.response.market.MarketingPageResponse;
import com.wanmi.sbc.marketing.bean.vo.MarketingPageVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 一口价
 */
@Api(tags = "MarketingBuyoutPriceController", description = "一口价营销服务API")
@RestController
@RequestMapping("/marketing/buyoutPrice")
@Validated
public class MarketingBuyoutPriceController {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private MarketingBuyoutPriceProvider marketingBuyoutPriceProvider;

    @Autowired
    private MarketingBuyoutPriceQueryProvider marketingBuyoutPriceQueryProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 新增一口价营销信息
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "新增一口价营销信息")
    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse add(@Valid @RequestBody MarketingBuyoutPriceAddRequest request) {
        request.valid();

        request.setIsBoss(BoolFlag.NO);
        request.setStoreId(commonUtil.getStoreId());
        request.setCreatePerson(commonUtil.getOperatorId());

        marketingBuyoutPriceProvider.add(request);

        // 更新es
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().skuIds(request.getSkuIds()).build());

        operateLogMQUtil.convertAndSend("营销", "创建一口价活动", "创建一口价活动：" + request.getMarketingName());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改一口价营销信息
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "修改一口价营销信息")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse modify(@Valid @RequestBody MarketingBuyoutPriceAddRequest request) {
        request.valid();

        request.setUpdatePerson(commonUtil.getOperatorId());

        marketingBuyoutPriceProvider.modify(request);

        // 更新es
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().skuIds(request.getSkuIds()).build());

        operateLogMQUtil.convertAndSend("营销", "编辑促销活动", "编辑促销活动：" + request.getMarketingName());
        return BaseResponse.SUCCESSFUL();
    }



    /**
     * 搜索营销活动
     * @return
     */
    @ApiOperation(value = "搜索营销活动")
    @RequestMapping(value = "/searchMarketing", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<MarketingPageVO>> searchMarketingList(@RequestBody MarketingBuyoutPriceSearchRequest marketingPageListRequest) {
        marketingPageListRequest.setStoreId(commonUtil.getStoreId());
        marketingPageListRequest.setPlatform(Platform.SUPPLIER);
        BaseResponse<MarketingPageResponse> pageResponse = marketingBuyoutPriceQueryProvider.search(marketingPageListRequest);
        return BaseResponse.success(pageResponse.getContext().getMarketingVOS());
    }

}
