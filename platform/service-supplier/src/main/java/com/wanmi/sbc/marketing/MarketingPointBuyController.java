package com.wanmi.sbc.marketing;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullReductionAddRequest;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/marketing/fullReduction")
public class MarketingPointBuyController {

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 新增满减营销信息
     * @param request
     * @return
     */
    @ApiOperation(value = "新增满减营销信息")
    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse add(@Valid @RequestBody MarketingFullReductionAddRequest request) {
        request.valid();

        request.setIsBoss(BoolFlag.NO);
        request.setStoreId(commonUtil.getStoreId());
        request.setCreatePerson(commonUtil.getOperatorId());

        marketingFullReductionProvider.add(request);

        // 更新es
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().skuIds(request.getSkuIds()).build());

        operateLogMQUtil.convertAndSend("营销","创建满减活动","创建满减活动："+request.getMarketingName());
        return BaseResponse.SUCCESSFUL();
    }
}
