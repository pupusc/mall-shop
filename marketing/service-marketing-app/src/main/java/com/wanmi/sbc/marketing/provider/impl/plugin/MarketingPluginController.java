package com.wanmi.sbc.marketing.provider.impl.plugin;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoDetailByGoodsInfoResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.MarketingPluginService;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsDetailFilterRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.response.info.GoodsInfoListByGoodsInfoResponse;
import com.wanmi.sbc.marketing.api.response.plugin.MarketingPluginGoodsDetailFilterResponse;
import com.wanmi.sbc.marketing.request.MarketingPluginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-21
 */
@RestController
@Validated
public class MarketingPluginController implements MarketingPluginProvider {

    @Autowired
    private MarketingPluginService marketingPluginService;


    /**
     * @param request 商品列表处理结构 {@link MarketingPluginGoodsListFilterRequest}
     * @return
     */
    @Override
    public BaseResponse<GoodsInfoListByGoodsInfoResponse> goodsListFilter(@RequestBody @Valid MarketingPluginGoodsListFilterRequest request) {
        GoodsInfoListByGoodsInfoResponse goodsInfoListByGoodsInfoResponse = marketingPluginService.goodsListFilter(KsBeanUtil.convertList(request.getGoodsInfos(), GoodsInfoVO.class),
                MarketingPluginRequest.builder().customer(KsBeanUtil.convert(request.getCustomerDTO(), CustomerVO.class)).moFangFlag(request.getMoFangFlag())
                        .isFlashSaleMarketing(request.getIsFlashSaleMarketing()).commitFlag(request.getCommitFlag()).isIndependent(request.getIsIndependent()).build());
        return BaseResponse.success(goodsInfoListByGoodsInfoResponse);
    }

    /**
     * @param request 商品详情处理结构 {@link MarketingPluginGoodsDetailFilterRequest}
     * @return
     */
    @Override
    public BaseResponse<MarketingPluginGoodsDetailFilterResponse> goodsDetailFilter(@RequestBody @Valid MarketingPluginGoodsDetailFilterRequest request) {
        GoodsInfoDetailByGoodsInfoResponse goodsInfoDetailByGoodsInfoResponse = marketingPluginService.goodsDetailFilter(KsBeanUtil.convert(request.getGoodsInfoDetailByGoodsInfoDTO(), GoodsInfoDetailByGoodsInfoResponse.class),
                MarketingPluginRequest.builder().customer(KsBeanUtil.convert(request.getCustomerDTO(), CustomerVO.class)).build());
        return BaseResponse.success(KsBeanUtil.convert(goodsInfoDetailByGoodsInfoResponse, MarketingPluginGoodsDetailFilterResponse.class));
    }

    @Override
    public BaseResponse<GoodsInfoListByGoodsInfoResponse> distributionGoodsListFilter(@RequestBody @Valid MarketingPluginGoodsListFilterRequest request) {
        GoodsInfoListByGoodsInfoResponse goodsInfoListByGoodsInfoResponse = marketingPluginService.distributionGoodsListFilter(KsBeanUtil.convertList(request.getGoodsInfos(), GoodsInfoVO.class),
                MarketingPluginRequest.builder().customer(KsBeanUtil.convert(request.getCustomerDTO(), CustomerVO.class)).moFangFlag(request.getMoFangFlag())
                        .isFlashSaleMarketing(request.getIsFlashSaleMarketing()).commitFlag(request.getCommitFlag()).build());
        return BaseResponse.success(goodsInfoListByGoodsInfoResponse);
    }

}
