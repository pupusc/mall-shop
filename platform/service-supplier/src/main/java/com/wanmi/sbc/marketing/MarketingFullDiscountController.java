package com.wanmi.sbc.marketing;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.marketing.api.provider.discount.MarketingFullDiscountProvider;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullDiscountAddRequest;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullDiscountModifyRequest;
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
 * 满折
 */
@Api(tags = "MarketingFullDiscountController", description = "满折服务API")
@RestController
@RequestMapping("/marketing/fullDiscount")
@Validated
public class MarketingFullDiscountController {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private MarketingFullDiscountProvider marketingFullDiscountProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 新增满折营销信息
     * @param request
     * @return
     */
    @ApiOperation(value = "新增满折营销信息")
    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse add(@Valid @RequestBody MarketingFullDiscountAddRequest request) {
        request.valid();

        request.setIsBoss(BoolFlag.NO);
        request.setStoreId(commonUtil.getStoreId());
        request.setCreatePerson(commonUtil.getOperatorId());

        marketingFullDiscountProvider.add(request);

        // 更新es
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().skuIds(request.getSkuIds()).build());

        operateLogMQUtil.convertAndSend("营销","创建满折活动","创建满折活动："+request.getMarketingName());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改满折营销信息
     * @param request
     * @return
     */
    @ApiOperation(value = "修改满折营销信息")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse modify(@Valid @RequestBody MarketingFullDiscountModifyRequest request) {
        request.valid();

        request.setUpdatePerson(commonUtil.getOperatorId());
        marketingFullDiscountProvider.modify(request);

        // 更新es
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().skuIds(request.getSkuIds()).build());

        operateLogMQUtil.convertAndSend("营销","编辑促销活动","编辑促销活动："+request.getMarketingName());
        return BaseResponse.SUCCESSFUL();
    }
}
