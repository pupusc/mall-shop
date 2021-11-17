package com.wanmi.sbc.marketing;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.marketing.api.provider.discount.MarketingPointBuyProvider;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullDiscountModifyRequest;
import com.wanmi.sbc.marketing.bean.dto.MarketingPointBuyAddRequest;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/marketing/point-buy")
public class MarketingPointBuyController {

    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private MarketingPointBuyProvider marketingPointBuyProvider;

    /**
     * 新增满减营销信息
     * @param request
     * @return
     */
    @ApiOperation(value = "积分换购活动")
    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse add(@RequestBody MarketingPointBuyAddRequest request) {
        request.setIsBoss(BoolFlag.NO);
        request.setStoreId(commonUtil.getStoreId());
        request.setCreatePerson(commonUtil.getOperatorId());
        marketingPointBuyProvider.add(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改满折营销信息
     * @param request
     * @return
     */
    @ApiOperation(value = "修改积分换购活动")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse modify(@RequestBody MarketingPointBuyAddRequest request) {
        request.setUpdatePerson(commonUtil.getOperatorId());
        marketingPointBuyProvider.update(request);
        return BaseResponse.SUCCESSFUL();
    }
}
