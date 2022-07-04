package com.soybean.mall.goods.controller;

import com.soybean.mall.common.CommonUtil;
import com.soybean.mall.goods.response.GoodsDetailResponse;
import com.soybean.mall.goods.vo.SpuSpecsParamVO;
import com.soybean.mall.goods.vo.SpuSpecsResultVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.api.constant.GoodsErrorCode;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsViewByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsCacheInfoByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoByIdRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsViewByIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoByIdResponse;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.request.market.MarketingGetByIdRequest;
import com.wanmi.sbc.marketing.api.response.market.MarketingGetByIdForCustomerResponse;
import com.wanmi.sbc.marketing.bean.vo.MarketingForEndVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController("wxGoodsController")
@RequestMapping("/wx/goods")
public class GoodsController {

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private MarketingQueryProvider marketingQueryProvider;

    /**
     * @description 商品详情页
     * @param spuId
     * @menu 小程序
     * @status done
     */
    @GetMapping("/detail")
    public BaseResponse<GoodsDetailResponse> detail(@RequestParam("spuId")String spuId, @RequestParam(value = "skuId",required = false)String skuId){
        GoodsViewByIdRequest request = new GoodsViewByIdRequest();
        request.setGoodsId(spuId);
        request.setShowLabelFlag(true);
        if(StringUtils.isEmpty(spuId)){
            BaseResponse<GoodsInfoByIdResponse> goodsInfo = goodsInfoQueryProvider.getById(GoodsInfoByIdRequest.builder().goodsInfoId(skuId).build());
            if(goodsInfo == null || goodsInfo.getContext() == null){
                throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
            }
            request.setGoodsId(goodsInfo.getContext().getGoodsId());
        }
        GoodsViewByIdResponse response = goodsQueryProvider.getViewById(request).getContext();
        return BaseResponse.success(KsBeanUtil.convert(response,GoodsDetailResponse.class));
    }

    /**
     * @menu 小程序
     * 购物车-spu规格信息
     */
    @RequestMapping(value = "/spu/specs", method = RequestMethod.POST)
    public BaseResponse<SpuSpecsResultVO> specs(@RequestBody @Valid SpuSpecsParamVO paramVO) {
        //获取源头
        CustomerVO customer = commonUtil.getCustomer();
        TerminalSource terminal = commonUtil.getTerminal();

        GoodsViewByIdResponse response = goodsDetailBaseInfoNew(paramVO.getSpuId(), customer.getCustomerId());
        if (CollectionUtils.isEmpty(response.getGoods().getGoodsChannelTypeSet())
                || !response.getGoods().getGoodsChannelTypeSet().contains(terminal.getCode().toString())) {
            throw new SbcRuntimeException("K-030025"); //K-030025=商品不可访问
        }

        List<GoodsInfoVO> goodsInfoVOList = response.getGoodsInfos().stream().filter(g -> {
            if (Objects.isNull(g.getProviderId()) && g.getAddedFlag() == AddedFlag.YES.toValue()) {
                return true;
            }
            if (Constants.yes.equals(g.getVendibility()) && g.getAddedFlag() == AddedFlag.YES.toValue()) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(goodsInfoVOList)) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }

        //过滤营销活动下的sku
        if (Objects.nonNull(paramVO.getMarketingId())) {
            MarketingGetByIdRequest mktParam = new MarketingGetByIdRequest();
            mktParam.setMarketingId(paramVO.getMarketingId());
            BaseResponse<MarketingGetByIdForCustomerResponse> mktResp = marketingQueryProvider.getByIdForCustomer(mktParam);
            if (mktResp == null || mktResp.getContext() == null || mktResp.getContext().getMarketingForEndVO() == null) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "指定的营销活动不存在");
            }
            MarketingForEndVO mkt = mktResp.getContext().getMarketingForEndVO();
            List<String> mktSkuIds = new ArrayList<>();
            if (mkt.getGoodsList() != null && mkt.getGoodsList().getGoodsInfos() != null) {
                for (GoodsInfoVO goodsInfo : mkt.getGoodsList().getGoodsInfos()) {
                    mktSkuIds.add(goodsInfo.getGoodsInfoId());
                }
            }
            goodsInfoVOList = goodsInfoVOList.stream().filter(item->mktSkuIds.contains(item.getGoodsInfoId())).collect(Collectors.toList());
        }

        SpuSpecsResultVO result = new SpuSpecsResultVO();
        result.setGoodsInfos(goodsInfoVOList);
        result.setGoodsSpecs(response.getGoodsSpecs());
        result.setGoodsSpecDetails(response.getGoodsSpecDetails());
        return BaseResponse.success(result);
    }

    /**
     * SPU商品详情-基础信息（不包括区间价、营销信息） 优化
     * @return SPU商品详情
     */
    private GoodsViewByIdResponse goodsDetailBaseInfoNew(String spuId, String customerId) {
        GoodsCacheInfoByIdRequest request = new GoodsCacheInfoByIdRequest();
        request.setCustomerId(customerId);
        request.setGoodsId(spuId);
        request.setShowLabelFlag(true);
        request.setShowSiteLabelFlag(true);
        return goodsQueryProvider.getCacheViewById(request).getContext();
    }
}
